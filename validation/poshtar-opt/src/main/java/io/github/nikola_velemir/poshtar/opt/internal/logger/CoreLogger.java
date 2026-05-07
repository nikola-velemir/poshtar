package io.github.nikola_velemir.poshtar.opt.internal.logger;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Base class for logger implementations. Class provides a default method for message logging.
 *
 * <p>
 * Classes extending this class provide their message kind,
 * or in other words, severity of the message.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
abstract class CoreLogger implements Logger {
    /**
     * Returns the diagnostic severity level for this logger implementation.
     *
     * @return The {@link javax.tools.Diagnostic.Kind} (e.g., ERROR, WARNING) to be used.
     */
    protected abstract Diagnostic.Kind getKind();

    /**
     * Reports a diagnostic message to the compiler's messager with a standard PoshtaR prefix.
     * <p>
     * This method aggregates the provided error message dispatches
     * it using the {@link javax.tools.Diagnostic.Kind} provided by the implementation's {@code getKind()} method.
     * It enables the IDE to provide "click-to-jump" functionality
     * directly to the source of the architectural violation.
     * </p>
     *
     * @param env          The current processing environment.
     * @param errorMessage The descriptive text explaining the violation or error.
     * @param element      The source code element (class, method, or field) to which this message applies.
     */
    public void log(ProcessingEnvironment env, String errorMessage, Element element) {
        String aggregatedMessage = "[PoshtaR] " + errorMessage;
        env.getMessager().printMessage(
                getKind(),
                aggregatedMessage,
                element);
    }
}
