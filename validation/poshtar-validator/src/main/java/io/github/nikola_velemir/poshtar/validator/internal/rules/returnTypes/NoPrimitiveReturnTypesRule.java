/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.validator.internal.rules.returnTypes;

import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.validator.base.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.base.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.base.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.validator.base.internal.rules.Rule;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Rule that warns the developers that return type is a primitive or a built-in wrapper class.
 *
 * <p>
 * Developer is to be discouraged from returning simple types like {@code int} or {@link String}.
 * It is advisable for a return type to be a custom, user defined object instead ofa primitive type.
 * Rule will warn the developer about the issue in question, instead of preventing compilation.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class NoPrimitiveReturnTypesRule implements Rule {

    private static final String PRIMITIVE_RETURN_TYPE_MESSAGE = "Request return type should not be a primitive.";
    private static final String BUILT_IN_RETURN_TYPE_MESSAGE = "Request return type '%s' is a built-in Java type. " + "It is advisable to use a custom DTO or Unit for better versioning safety.";
    private static final Logger logger = LoggerProvider.provideWarningLogger();

    /**
     * Validates the request's response type,
     * giving a warning if response type is a primitive or a built-in class.
     *
     * @param roundEnv The environment providing access to elements in the current processing round.
     * @param ctx      Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        var elementUtils = ctx.getElements();
        var typeUtils = ctx.getTypes();

        var mapType = typeUtils.erasure(elementUtils.getTypeElement(Map.class.getCanonicalName()).asType());
        var collectionType = typeUtils.erasure(elementUtils.getTypeElement(Collection.class.getCanonicalName()).asType());

        for (var requestFqn : ctx.getKnownRequests()) {
            TypeElement typeElement = elementUtils.getTypeElement(requestFqn);

            if (typeElement == null) continue;

            TypeMirror responseType = resolveResponseType(typeElement, ctx);
            if (responseType == null || responseType.getKind() == TypeKind.VOID) continue;

            validateReturnType(ctx, responseType, typeElement, collectionType, mapType);
        }
    }

    private static void validateReturnType(ProcessorContext ctx, TypeMirror responseType, TypeElement targetClass, TypeMirror collectionType, TypeMirror mapType) {
        if (validateIfPrimitive(ctx, responseType, targetClass)) return;

        if (validateComplexReturn(ctx, responseType, targetClass, collectionType, mapType)) return;
        Element element = ctx.getTypes().asElement(responseType);
        if (!(element instanceof TypeElement te)) return;

        String fqn = te.getQualifiedName().toString();
        if (!fqn.startsWith("java.")) return;

        String errorMessage = String.format(BUILT_IN_RETURN_TYPE_MESSAGE, fqn);

        logger.log(ctx.env, errorMessage, targetClass);
    }

    private static boolean validateComplexReturn(ProcessorContext ctx, TypeMirror responseType, TypeElement targetClass, TypeMirror collectionType, TypeMirror mapType) {
        if (responseType instanceof DeclaredType declaredType) {
            TypeMirror erasedResponse = ctx.getTypes().erasure(responseType);

            if (ctx.getTypes().isAssignable(erasedResponse, mapType)) {
                List<? extends TypeMirror> args = declaredType.getTypeArguments();
                if (args.size() == 2) {
                    validateReturnType(ctx, args.get(1), targetClass, collectionType, mapType);
                }
                return true;
            }

            // 3. Handle Collections (Validate the element)
            if (ctx.getTypes().isAssignable(erasedResponse, collectionType)) {
                declaredType.getTypeArguments().forEach(arg ->
                        validateReturnType(ctx, arg, targetClass, collectionType, mapType));
                return true;
            }
        }
        return false;
    }

    private static boolean validateIfPrimitive(ProcessorContext ctx, TypeMirror responseType, TypeElement targetClass) {
        if (!responseType.getKind().isPrimitive()) return false;

        logger.log(ctx.env, PRIMITIVE_RETURN_TYPE_MESSAGE, targetClass);
        return true;
    }

    private TypeMirror findGenericArgument(TypeElement element, Types typeUtils, Elements elementUtils) {
        // Check direct interfaces
        for (TypeMirror iface : element.getInterfaces()) {
            if (iface instanceof DeclaredType declared) {
                Element ifaceElement = declared.asElement();
                if (ifaceElement instanceof TypeElement te && te.getQualifiedName().contentEquals(Request.class.getName())) {
                    return declared.getTypeArguments().get(0);
                }
            }
        }

        return findGenericArgumentRecursively(element, typeUtils, elementUtils);
    }

    private TypeMirror resolveResponseType(TypeElement element, ProcessorContext ctx) {
        TypeMirror requestInterface = ctx.getElements().getTypeElement(Request.class.getCanonicalName()).asType();

        for (TypeMirror iface : ctx.getTypes().directSupertypes(element.asType())) {
            if (ctx.getTypes().isAssignable(ctx.getTypes().erasure(iface), ctx.getTypes().erasure(requestInterface))) {
                if (iface instanceof DeclaredType declared && !declared.getTypeArguments().isEmpty()) {
                    return declared.getTypeArguments().get(0);
                }
            }
        }

        // Check superclass recursively
        TypeMirror superclass = element.getSuperclass();
        if (superclass.getKind() != TypeKind.NONE && superclass instanceof DeclaredType) {
            return resolveResponseType((TypeElement) ((DeclaredType) superclass).asElement(), ctx);
        }

        return null;
    }


    private TypeMirror findGenericArgumentRecursively(TypeElement element, Types typeUtils, Elements elementUtils) {
        TypeMirror superclass = element.getSuperclass();
        if (superclass.getKind() != TypeKind.NONE) {
            Element superElement = typeUtils.asElement(superclass);
            if (superElement instanceof TypeElement te) {
                return findGenericArgument(te, typeUtils, elementUtils);
            }
        }

        return null;
    }
}
