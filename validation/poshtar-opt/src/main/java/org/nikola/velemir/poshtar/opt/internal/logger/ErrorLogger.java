package org.nikola.velemir.poshtar.opt.internal.logger;

import javax.tools.Diagnostic;

public class ErrorLogger extends CoreLogger {

    private static ErrorLogger instance;

    private ErrorLogger() {
    }

    public static ErrorLogger getInstance() {
        if (instance == null) {
            instance = new ErrorLogger();
        }
        return instance;
    }

    @Override
    protected Diagnostic.Kind getKind() {
        return Diagnostic.Kind.ERROR;
    }

}
