/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.nikola_velemir.poshtar.opt.internal.registry.scanner;

import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.opt.internal.registry.exception.ResolutionException;
import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * Internal utility class for type introspection during the scanning phase.
 * <p>
 * This helper provides static methods to extract metadata from {@link TypeElement}s,
 * specifically focusing on resolving generic type arguments from the {@code RequestHandler}
 * interface and locating specific {@link AnnotationMirror} instances.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class RegistryTypeHelper {
    private static final String REQUEST_HANDLER_INTERFACE_NAME = RequestHandler.class.getName();
    public static final String RESOLUTION_ERROR_MESSAGE = "PoshtaR: Cannot resolve request type for handle %s. Ensure the Request class is imported and compiles.";
    /**
     * Finds a specific annotation on an element and returns its mirror.
     *
     * @param element    The element (class, method, etc.) to inspect.
     * @param annotation The fully qualified name of the annotation to find.
     * @return The {@link AnnotationMirror} if found, otherwise {@code null}.
     */
    public static AnnotationMirror getAnnotationMirror(TypeElement element, String annotation) {
        return element.getAnnotationMirrors().stream()
                .filter(m -> m.getAnnotationType().toString().equals(annotation))
                .findFirst()
                .orElse(null);
    }
    /**
     * Inspects a handler class to determine the Fully Qualified Name of the request it handles.
     *
     * <p><b>Type Safety:</b></p>
     * <p>
     * If the request type is unresolved (e.g., the Request class is missing or has syntax errors),
     * a {@link ResolutionException} is thrown to prevent the processor from registering
     * "broken" handlers.
     * </p>
     *
     * @param handler The class element suspected of being a RequestHandler.
     * @param ctx     The current processor context for type and element utilities.
     * @return The FQN of the Request type as a String, or {@code null} if the interface is not found.
     * @throws ResolutionException if the Request type is in an error state (TypeKind.ERROR).
     */
    public static String extractRequestType(TypeElement handler, ProcessorContext ctx) throws ResolutionException {
        var typeUtils = ctx.env.getTypeUtils();
        var elementUtils = ctx.env.getElementUtils();

        TypeElement reqHandlerInterface = elementUtils.getTypeElement(REQUEST_HANDLER_INTERFACE_NAME);
        if (reqHandlerInterface == null) return null;

        TypeMirror erasedReqHandler = typeUtils.erasure(reqHandlerInterface.asType());

        for (TypeMirror iface : handler.getInterfaces()) {
            if (typeUtils.isAssignable(typeUtils.erasure(iface), erasedReqHandler)) {
                if (iface instanceof DeclaredType declared) {
                    List<? extends TypeMirror> typeArgs = declared.getTypeArguments();
                    if (typeArgs.isEmpty()) continue;

                    TypeMirror requestType = typeArgs.getFirst();

                    if (requestType.getKind() == TypeKind.ERROR) {
                        Name handlerName = handler.getSimpleName();
                        String errorMessage = String.format(RESOLUTION_ERROR_MESSAGE, handlerName);
                        throw new ResolutionException(errorMessage);
                    }
                    return typeUtils.erasure(requestType).toString();
                }
            }
        }
        return null;
    }
}
