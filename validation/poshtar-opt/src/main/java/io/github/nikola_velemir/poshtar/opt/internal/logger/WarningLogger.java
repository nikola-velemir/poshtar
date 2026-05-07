package io.github.nikola_velemir.poshtar.opt.internal.logger;

import javax.tools.Diagnostic;
/**
 * Warning logger class that dispatches messages that will notify of violations which are not critical for library function.
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class WarningLogger extends CoreLogger {

    public WarningLogger() {
    }
    /**
     * Returns the diagnostic severity level for this logger implementation.
     *
     * @return Returns {@link javax.tools.Diagnostic.Kind} of MANDATORY_WARNING.
     */
    @Override
    protected Diagnostic.Kind getKind() {
        return Diagnostic.Kind.MANDATORY_WARNING;
    }

}
