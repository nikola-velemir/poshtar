package org.nikola.velemir.poshtar.opt.processor.utils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class ErrorLogger {
    public static void logError(ProcessingEnvironment env, String errorMessage) {
        env.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                errorMessage);
    }
    public static void logError(ProcessingEnvironment env, String errorMessage, Element element) {
        env.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                errorMessage,
                element);
    }
}
