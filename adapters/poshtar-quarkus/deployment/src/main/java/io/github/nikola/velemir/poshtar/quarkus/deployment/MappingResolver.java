package io.github.nikola.velemir.poshtar.quarkus.deployment;

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.jboss.jandex.ParameterizedType;
import org.jspecify.annotations.NonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.nikola.velemir.poshtar.quarkus.deployment.ProcessorConstants.PIPELINE_BEHAVIOUR_CLASS_NAME;

class MappingResolver {
    public static @NonNull Map<String, List<String>> mapHandlerToBehaviours(Map<String, String> handlerToRequest, Map<String, String> behaviourToRequest, IndexView idx) {
        Map<String, List<String>> handlerToBehaviours = new LinkedHashMap<>();
        handlerToRequest.forEach((handlerName, requestName) -> {
            List<String> matching = behaviourToRequest.entrySet().stream()
                    .filter(e -> {
                        String supportedRequest = e.getValue();
                        if (supportedRequest == null) return true; // global
                        return isAssignableFrom(idx, supportedRequest, requestName);
                    })
                    .map(Map.Entry::getKey)
                    .toList();
            handlerToBehaviours.put(handlerName, matching);
        });
        return handlerToBehaviours;
    }

    private static boolean isAssignableFrom(IndexView idx, String superName, String subName) {
        if (superName.equals(subName)) return true;
        ClassInfo ci = idx.getClassByName(DotName.createSimple(subName));
        if (ci == null) return false;
        // Check superclass
        if (ci.superName() != null && isAssignableFrom(idx, superName, ci.superName().toString())) return true;
        // Check interfaces
        return ci.interfaceNames().stream()
                .anyMatch(iface -> isAssignableFrom(idx, superName, iface.toString()));
    }

    public static Map<String, String> resolveBehaviourMap(IndexView idx) {
        Map<String, String> result = new LinkedHashMap<>();

        for (ClassInfo ci : idx.getAllKnownImplementations(DotName.createSimple(PIPELINE_BEHAVIOUR_CLASS_NAME))) {
            for (org.jboss.jandex.Type iface : ci.interfaceTypes()) {
                if (!iface.name().toString().equals(PIPELINE_BEHAVIOUR_CLASS_NAME)) continue;
                if (!(iface instanceof org.jboss.jandex.ParameterizedType pt)) continue;

                org.jboss.jandex.Type arg = pt.arguments().get(0);

                if (arg.kind() == org.jboss.jandex.Type.Kind.TYPE_VARIABLE) {
                    result.put(ci.name().toString(), null);
                } else {
                    result.put(ci.name().toString(), arg.name().toString());
                }
                break;
            }
        }

        return result;
    }

    public static Map<String, String> resolveHandlerMap(IndexView idx, String handlerInterface, String markerInterface) {
        Map<String, String> result = new LinkedHashMap<>();

        for (ClassInfo ci : idx.getAllKnownImplementations(DotName.createSimple(handlerInterface))) {
            for (org.jboss.jandex.Type iface : ci.interfaceTypes()) {
                if (!iface.name().toString().equals(handlerInterface)) continue;
                if (!(iface instanceof ParameterizedType pt)) continue;

                org.jboss.jandex.Type arg = pt.arguments().get(0);
                String argName = arg.name().toString();

                if (idx.getClassByName(DotName.createSimple(argName)) != null
                        && idx.getAllKnownImplementations(DotName.createSimple(markerInterface))
                        .stream()
                        .anyMatch(c -> c.name().toString().equals(argName))) {
                    result.put(ci.name().toString(), argName);
                    break;
                }
            }
        }

        return result;
    }
}
