package io.github.nikola_velemir.poshtar.opt.internal.logger;

import javax.tools.Diagnostic;
/**
 * Error logger class that dispatches messages that will prevent project compilation (high severity).
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class ErrorLogger extends CoreLogger {

    public ErrorLogger() {
    }

    /**
     * Returns the diagnostic severity level for this logger implementation.
     *
     * @return Returns {@link javax.tools.Diagnostic.Kind} of ERROR.
     */
    @Override
    protected Diagnostic.Kind getKind() {
        return Diagnostic.Kind.ERROR;
    }

}
