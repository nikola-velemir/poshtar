package org.nikola.velemir.poshtar.opt.internal.rules;

import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.opt.internal.logger.WarningLogger;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

class NoPrimitiveReturnTypesRule implements Rule {

    public static final String PRIMITIVE_RETURN_TYPE_MESSAGE = "Request return type should not be a primitive.";
    public static final String BUILT_IN_RETURN_TYPE_MESSAGE = "Request return type '%s' is a built-in Java type. " +
            "It is advisable to use a custom DTO or Unit for better versioning safety.";

    @Override
    public void validate(RoundEnvironment roundEnv, RuleContext ctx) {
        var elementUtils = ctx.getElements();
        var typeUtils = ctx.getTypes();
        for (var requestFqn : ctx.getKnownRequests()) {
            TypeElement typeElement = elementUtils.getTypeElement(requestFqn);

            if (typeElement == null) continue;

            TypeMirror responseType = findGenericArgument(typeElement, typeUtils, elementUtils);
            if (responseType == null) continue;

            validateReturnType(ctx, responseType, typeElement);
        }
    }

    private static void validateReturnType(RuleContext ctx, TypeMirror responseType, TypeElement targetClass) {
        if (responseType.getKind().isPrimitive()) {
            WarningLogger.log(ctx.env, PRIMITIVE_RETURN_TYPE_MESSAGE, targetClass);
            return;
        }
        Element element = ctx.getTypes().asElement(responseType);
        if (!(element instanceof TypeElement te)) return;

        String fqn = te.getQualifiedName().toString();
        if (!fqn.startsWith("java.")) return;

        String errorMessage = String.format(BUILT_IN_RETURN_TYPE_MESSAGE, fqn);

        WarningLogger.log(ctx.env, errorMessage, targetClass);
    }

    private TypeMirror findGenericArgument(TypeElement element, Types typeUtils, Elements elementUtils) {
        // Check direct interfaces
        for (TypeMirror iface : element.getInterfaces()) {
            if (iface instanceof DeclaredType declared) {
                Element ifaceElement = declared.asElement();
                if (ifaceElement instanceof TypeElement te
                        && te.getQualifiedName().contentEquals(Request.class.getName())) {
                    return declared.getTypeArguments().getFirst();
                }
            }
        }

        return findGenericArgumentRecursively(element, typeUtils, elementUtils);
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
