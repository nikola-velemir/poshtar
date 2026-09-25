package io.github.nikola_velemir.poshtar.validator.internal;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Used to check the class, interface and type hierarchies to ensure backward compatibility.
 */
public class GenericsHelper {
    /**
     * Checks if {@code type} implements or extends {@code erasedTarget}
     * anywhere in its supertype hierarchy (erasure-safe).
     *
     * @param type         Designated type.
     * @param erasedTarget Type lost thru erasure.
     * @param typeUtils    Type utils of the processor context.
     * @return True or false whether the designated type implements the target.
     */
    public static boolean implementsHierarchically(TypeMirror type,
                                                   TypeMirror erasedTarget,
                                                   Types typeUtils) {
        if (type == null || erasedTarget == null) {
            return false;
        }

        // isAssignable walks superclasses + interfaces recursively automatically!
        return typeUtils.isAssignable(
                typeUtils.erasure(type),
                typeUtils.erasure(erasedTarget)
        );
    }

    /**
     * Walks the full supertype hierarchy (superclasses and interfaces,
     * recursively) of {@code type} looking for {@code erasedTarget}.
     * Needed because e.g. a class implementing Command or VoidCommand
     * doesn't implement Request directly — it's one or more hops up.
     *
     * @param type         Designated type.
     * @param erasedTarget Type lost thru erasure.
     * @param typeUtils    Type utils of the processor context.
     * @param visited      Visited classes
     * @return True or false whether the designated type implements the target.
     */
    public static boolean implementsHierarchically(TypeMirror type,
                                                   TypeMirror erasedTarget,
                                                   Types typeUtils,
                                                   Set<String> visited) {
        TypeMirror erasedType = typeUtils.erasure(type);

        if (typeUtils.isSameType(erasedType, erasedTarget)) {
            return true;
        }

        if (!visited.add(erasedType.toString())) {
            return false; // already visited, avoid cycles/diamond re-walk
        }

        for (TypeMirror supertype : typeUtils.directSupertypes(type)) {
            if (implementsHierarchically(supertype, erasedTarget, typeUtils, visited)) {
                return true;
            }
        }

        return false;
    }
}
