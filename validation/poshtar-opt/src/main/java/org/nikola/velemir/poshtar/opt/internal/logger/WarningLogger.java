package org.nikola.velemir.poshtar.opt.internal.logger;

import javax.tools.Diagnostic;

class WarningLogger extends CoreLogger {
    private static WarningLogger instance;

    public WarningLogger() {
    }

    @Override
    protected Diagnostic.Kind getKind() {
        return Diagnostic.Kind.MANDATORY_WARNING;
    }

}
