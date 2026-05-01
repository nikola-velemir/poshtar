package io.github.nikola_velemir.poshtar.opt.internal.logger;

import javax.tools.Diagnostic;

class ErrorLogger extends CoreLogger {

    private static ErrorLogger instance;

    public ErrorLogger() {
    }

    @Override
    protected Diagnostic.Kind getKind() {
        return Diagnostic.Kind.ERROR;
    }

}
