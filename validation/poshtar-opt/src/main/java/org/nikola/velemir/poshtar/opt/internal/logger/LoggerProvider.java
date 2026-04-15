package org.nikola.velemir.poshtar.opt.internal.logger;

public class LoggerProvider {
    private static final ErrorLogger errorLogger = new ErrorLogger();
    private static final WarningLogger warningLogger = new WarningLogger();

    public static Logger provideErrorLogger() {
        return errorLogger;
    }

    public static Logger provideWarningLogger() {
        return warningLogger;
    }

}
