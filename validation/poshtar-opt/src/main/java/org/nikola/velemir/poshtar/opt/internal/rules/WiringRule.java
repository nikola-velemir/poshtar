package org.nikola.velemir.poshtar.opt.internal.rules;

import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.opt.internal.logger.ErrorLogger;
import org.nikola.velemir.poshtar.opt.processor.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

@SuppressWarnings({"rawtypes", "unchecked"})
abstract class WiringRule implements Rule {
    protected final Class annotation;
    protected final ErrorLogger logger = ErrorLogger.getInstance();

    public WiringRule(Class annotation) {
        this.annotation = annotation;
    }

    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        initErasures(ctx);

        for (var element : roundEnv.getRootElements()) {
            if (element instanceof TypeElement typeElement && element.getKind().equals(ElementKind.CLASS)) {
                validateAnnotationAndImplementation(ctx, typeElement);
            }
        }
    }

    protected abstract void validateAnnotationAndImplementation(ProcessorContext ctx, TypeElement typeElement);

    protected abstract void initErasures(ProcessorContext ctx);

    protected boolean hasAnnotation(TypeElement typeElement) {
        return typeElement.getAnnotation(annotation) != null;
    }
}
