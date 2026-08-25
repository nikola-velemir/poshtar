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

package io.github.nikola_velemir.poshtar.validator.base.internal.registry.scanner;


import io.github.nikola_velemir.poshtar.validator.base.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.base.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.base.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.validator.base.internal.registry.exception.ResolutionException;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.request.Request;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Implementation of {@link RegistryScanner} that performs classpath
 * discovery during an annotation processing round.
 * <p>
 * This scanner identifies three primary categories of components:
 * <ul>
 *     <li><b>Handlers:</b> Classes annotated with {@code @Handler} that process specific requests.</li>
 *     <li><b>Behaviors:</b> Classes annotated with {@code @Behaviour} that act as pipeline middleware.</li>
 *     <li><b>Requests:</b> Any class or record implementing the {@code Request} interface.</li>
 * </ul>
 * </p>
 * <p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class RegistryScannerImpl implements RegistryScanner {
    private static final String HANDLER_ANNOTATION_NAME = Handler.class.getName();
    private static final String BEHAVIOUR_ANNOTATION_NAME = Behaviour.class.getName();

    private static final CharSequence NOTIFICATION_INTERFACE_NAME = Notification.class.getName();
    private static final CharSequence REQUEST_INTERFACE_NAME = Request.class.getName();
    private final Logger logger = LoggerProvider.provideErrorLogger();

    /**
     * Controls the scanning process for the current processing round.
     *
     * @param roundEnv The environment representing the current round of annotation processing.
     * @param ctx      The shared context where discovered components are stored.
     * @throws ResolutionException is thrown if component resolution fails.
     */
    public void scanRegistry(RoundEnvironment roundEnv, ProcessorContext ctx) {

        scanForHandlers(roundEnv, ctx);

        scanForBehaviours(roundEnv, ctx);

        scanForRequests(roundEnv, ctx);

        scanForNotifications(roundEnv, ctx);
    }

    private void scanForRequests(RoundEnvironment roundEnv, ProcessorContext ctx) {
        TypeElement requestInterface = ctx.getElements()
                .getTypeElement(REQUEST_INTERFACE_NAME);
        if (requestInterface == null) return;

        TypeMirror erasedRequest = ctx.env.getTypeUtils()
                .erasure(requestInterface.asType());

        roundEnv.getRootElements().stream()
                .filter(e -> e.getKind() == ElementKind.CLASS || e.getKind() == ElementKind.RECORD)
                .map(e -> (TypeElement) e)
                .filter(e -> ctx.env.getTypeUtils().isAssignable(
                        ctx.env.getTypeUtils().erasure(e.asType()),
                        erasedRequest
                ))
                .forEach(e -> ctx.registerRequest(e.getQualifiedName().toString()));
    }
    private void scanForNotifications(RoundEnvironment roundEnv, ProcessorContext ctx) {
        TypeElement requestInterface = ctx.getElements()
                .getTypeElement(NOTIFICATION_INTERFACE_NAME);
        if (requestInterface == null) return;

        TypeMirror erasedNotification = ctx.env.getTypeUtils()
                .erasure(requestInterface.asType());

        roundEnv.getRootElements().stream()
                .filter(e -> e.getKind() == ElementKind.CLASS || e.getKind() == ElementKind.RECORD)
                .map(e -> (TypeElement) e)
                .filter(e -> ctx.env.getTypeUtils().isAssignable(
                        ctx.env.getTypeUtils().erasure(e.asType()),
                        erasedNotification
                ))
                .forEach(e -> ctx.registerNotification(e.getQualifiedName().toString()));
    }

    private void scanForBehaviours(RoundEnvironment roundEnv, ProcessorContext ctx) {
        TypeElement behaviourAnnot = ctx.getElements().getTypeElement(BEHAVIOUR_ANNOTATION_NAME);
        if (behaviourAnnot == null) return;

        roundEnv.getElementsAnnotatedWith(behaviourAnnot).stream()
                .filter(e -> e.getKind() == ElementKind.CLASS)
                .map(e -> (TypeElement) e)
                .forEach(b -> {
                    AnnotationMirror mirror = RegistryTypeHelper.getAnnotationMirror(b, BEHAVIOUR_ANNOTATION_NAME);
                    ctx.registerBehaviour(b.getQualifiedName().toString(), b, mirror);
                });
    }

    private void scanForHandlers(RoundEnvironment roundEnv, ProcessorContext ctx) {
        TypeElement handlerAnnot = ctx.getElements().getTypeElement(HANDLER_ANNOTATION_NAME);
        if (handlerAnnot == null) return;
        roundEnv.getElementsAnnotatedWith(handlerAnnot).stream()
                .filter(e -> e.getKind() == ElementKind.CLASS)
                .map(e -> (TypeElement) e)
                .forEach(h -> {
                    try {
                        registerHandler(ctx, h);
                    } catch (ResolutionException ex) {
                        logError(h, ctx, ex);
                    }
                });
    }

    private void registerHandler(ProcessorContext ctx, TypeElement h) {
        String requestType = RegistryTypeHelper.extractRequestType(h, ctx);
        if (requestType != null) {
            var mirror = RegistryTypeHelper.getAnnotationMirror(h, HANDLER_ANNOTATION_NAME);
            ctx.registerHandler(h.getQualifiedName().toString(), requestType, h, mirror);
        }
    }


    private void logError(TypeElement handler, ProcessorContext ctx, ResolutionException ex) {
        logger.log(ctx.env, ex.getMessage(), handler);
    }

}
