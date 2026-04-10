package org.nikola.velemir.poshtar.opt.internal.logger;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class CoreLogger {

    public static void log(Diagnostic.Kind errorKind, ProcessingEnvironment env, String errorMessage, Element element) {
        env.getMessager().printMessage(
                errorKind,
                errorMessage,
                element);
    }
}
