package org.nikola.velemir.poshtar.opt.internal.logger;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

abstract class CoreLogger {

    protected abstract Diagnostic.Kind getKind();

    public void log(ProcessingEnvironment env, String errorMessage, Element element) {
        env.getMessager().printMessage(
                getKind(),
                errorMessage,
                element);
    }
}
