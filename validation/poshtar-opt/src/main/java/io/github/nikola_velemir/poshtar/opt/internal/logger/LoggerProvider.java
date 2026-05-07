package io.github.nikola_velemir.poshtar.opt.internal.logger;

/**
 * Provider that manages the instantiation and retrieval of {@link Logger} implementations.
 * <p>
 * This class serves as an injector for the {@link Logger} implementations used by the
 * {@link io.github.nikola_velemir.poshtar.opt.internal.rules.Rule} implementations.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class LoggerProvider {
    private static final ErrorLogger errorLogger = new ErrorLogger();
    private static final WarningLogger warningLogger = new WarningLogger();

    /**
     * Provides an instance of {@link ErrorLogger}, used by rule classes.
     *
     * @return Ready instance of {@link  ErrorLogger}
     */
    public static Logger provideErrorLogger() {
        return errorLogger;
    }

    /**
     * Provides an instance of {@link WarningLogger}, used by rule classes.
     *
     * @return Ready instance of {@link  WarningLogger}
     */
    public static Logger provideWarningLogger() {
        return warningLogger;
    }

}
