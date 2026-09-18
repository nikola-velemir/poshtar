package io.github.nikola_velemir.poshtar.validator.internal;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GenericsHelper {

    /**
     * Walks the full supertype hierarchy (superclasses and interfaces,
     * recursively) of {@code type} looking for {@code erasedTarget}.
     * Needed because e.g. a class implementing Command<T> or VoidCommand
     * doesn't implement Request directly — it's one or more hops up.
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
