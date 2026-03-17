package com.example.demo.shared.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {
    private static final Logger log = LoggerFactory.getLogger(LoggingService.class);

    public void logActivity(String type, String message) {
        log.info("🚀 [POSHTAR-{}] : {}", type.toUpperCase(), message);
    }

    public void logError(String message, Throwable t) {
        log.error("❌ [POSHTAR-ERROR] : {}", message, t);
    }
}
