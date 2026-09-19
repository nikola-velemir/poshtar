package io.github.nikola.velemir.poshtar.quarkus.deployment;

import org.jboss.jandex.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class GenericsHelper {

    public static boolean isAssignableFrom(IndexView idx, String superName, String subName) {
        if (superName.equals(subName)) return true;
        ClassInfo ci = idx.getClassByName(DotName.createSimple(subName));

        if (ci == null) return false;

        if (ci.superName() != null && isAssignableFrom(idx, superName, ci.superName().toString())) return true;

        return ci.interfaceNames().stream()
                .anyMatch(iface -> isAssignableFrom(idx, superName, iface.toString()));
    }

    /**
     * Entry point: looks for {@code targetInterface} among {@code ci}'s directly implemented
     * interfaces and its superclass, recursing further up the hierarchy (through interfaces
     * extending other interfaces, and superclasses) until it is found, resolving generic type
     * variables along the way.
     */
    public static org.jboss.jandex.Type resolveGenericArgument(IndexView idx, ClassInfo ci, DotName targetInterface) {
        for (org.jboss.jandex.Type iface : ci.interfaceTypes()) {
            org.jboss.jandex.Type resolved = resolveGenericArgument(idx, iface, targetInterface, Map.of());
            if (resolved != null) return resolved;
        }
        org.jboss.jandex.Type superType = ci.superClassType();
        if (superType != null) {
            return resolveGenericArgument(idx, superType, targetInterface, Map.of());
        }
        return null;
    }

    /**
     * Resolves {@code type} (a use of some interface/class, e.g. {@code QueryHandler<MyRequest>})
     * against {@code targetInterface}, applying {@code substitution} to translate any type
     * variables in {@code type} into what they resolve to from the original call site.
     */
    public static org.jboss.jandex.Type resolveGenericArgument(IndexView idx, org.jboss.jandex.Type type,
                                                                DotName targetInterface,
                                                                Map<String, org.jboss.jandex.Type> substitution) {
        org.jboss.jandex.Type resolvedType = substitute(type, substitution);

        if (resolvedType.name().equals(targetInterface)) {
            if (resolvedType instanceof ParameterizedType pt && !pt.arguments().isEmpty()) {
                return pt.arguments().get(0);
            }
            return null;
        }

        ClassInfo ci = idx.getClassByName(resolvedType.name());
        if (ci == null) return null;

        // Map ci's own type parameters (e.g. Q in "interface QueryHandler<Q>") to whatever
        // concrete/variable types were passed in `resolvedType` (e.g. MyRequest).
        Map<String, org.jboss.jandex.Type> nextSubstitution = buildSubstitution(ci, resolvedType);

        for (org.jboss.jandex.Type iface : ci.interfaceTypes()) {
            org.jboss.jandex.Type result = resolveGenericArgument(idx, iface, targetInterface, nextSubstitution);
            if (result != null) return result;
        }
        if (ci.superClassType() != null) {
            return resolveGenericArgument(idx, ci.superClassType(), targetInterface, nextSubstitution);
        }
        return null;
    }

    private static Map<String, org.jboss.jandex.Type> buildSubstitution(ClassInfo ci, org.jboss.jandex.Type usage) {
        if (!(usage instanceof ParameterizedType pt)) return Map.of();
        List<TypeVariable> params = ci.typeParameters();
        Map<String, org.jboss.jandex.Type> map = new LinkedHashMap<>();
        for (int i = 0; i < params.size() && i < pt.arguments().size(); i++) {
            map.put(params.get(i).name().toString(), pt.arguments().get(i));
        }
        return map;
    }

    private static org.jboss.jandex.Type substitute(org.jboss.jandex.Type type, Map<String, org.jboss.jandex.Type> substitution) {
        if (substitution.isEmpty()) return type;

        if (type.kind() == org.jboss.jandex.Type.Kind.TYPE_VARIABLE) {
            org.jboss.jandex.Type replacement = substitution.get(type.name().toString());
            return replacement != null ? replacement : type;
        }

        if (type instanceof ParameterizedType pt) {
            List<org.jboss.jandex.Type> substitutedArgs = pt.arguments().stream()
                    .map(arg -> substitute(arg, substitution))
                    .toList();
            return ParameterizedType.create(pt.name(), substitutedArgs.toArray(new org.jboss.jandex.Type[0]), pt.owner());
        }

        return type;
    }
}
