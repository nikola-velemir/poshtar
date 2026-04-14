package org.nikola.velemir.poshtar.opt.internal.logger;

import javax.tools.Diagnostic;

public class WarningLogger extends CoreLogger {
    private static WarningLogger instance;

    private WarningLogger() {
    }

    public static WarningLogger getInstance() {
        if (instance == null) {
            instance = new WarningLogger();
        }
        return instance;
    }

    @Override
    protected Diagnostic.Kind getKind() {
        return Diagnostic.Kind.MANDATORY_WARNING;
    }

}
