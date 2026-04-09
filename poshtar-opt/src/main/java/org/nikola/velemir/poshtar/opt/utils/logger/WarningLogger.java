package org.nikola.velemir.poshtar.opt.utils.logger;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class WarningLogger {
    private static final Diagnostic.Kind errorKind = Diagnostic.Kind.MANDATORY_WARNING;

    public static void log(ProcessingEnvironment env, String errorMessage, Element element) {
        CoreLogger.log(errorKind,env, errorMessage, element);
    }
}
