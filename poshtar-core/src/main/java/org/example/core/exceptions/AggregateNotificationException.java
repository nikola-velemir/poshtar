package org.example.core.exceptions;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

public class AggregateNotificationException extends PoshtarException {
    private final List<Throwable> errors;
    public AggregateNotificationException(List<Throwable> errors) {
        super(formatMessage(errors));
        this.errors = List.copyOf(errors);
    }

    public List<Throwable> getErrors() {
        return errors;
    }

    private static String formatMessage(List<Throwable> errors) {
        StringBuilder sb = new StringBuilder();
        sb.append(errors.size()).append(" failures occurred during notification dispatch:\n");
        for (int i = 0; i < errors.size(); i++) {
            sb.append("  [#").append(i + 1).append("] ")
                    .append(errors.get(i).getClass().getSimpleName()).append(": ")
                    .append(errors.get(i).getMessage()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        errors.forEach(e -> {
            s.println("\n--- Sub-exception details ---");
            e.printStackTrace(s);
        });
    }
}
