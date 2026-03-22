package demo.logs.service;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class LoggingService {
    private static final Logger log = LoggerFactory.getLogger(LoggingService.class);

    public void logActivity(String type, String message) {
        log.info("🚀 [POSHTAR-{}] : {}", type.toUpperCase(), message);
    }

    public void logError(String message, Throwable t) {
        log.error("❌ [POSHTAR-ERROR] : {}", message, t);
    }
}
