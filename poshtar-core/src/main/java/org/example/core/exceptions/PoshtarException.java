package org.example.core.exceptions;

public class PoshtarException extends RuntimeException {
    public PoshtarException(String message) {
        super("[PoshtaR] " + message);
    }

    public PoshtarException(String message, Throwable cause) {
        super("[PoshtaR] " + message, cause);
    }
}