package com.example.demo.infra.scheduler.handler;

import com.example.demo.shared.logger.LoggingService;
import com.example.demo.infra.scheduler.notification.ScheduledNotification;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

@Handler
public class NumeroUnoNotificationHandler implements NotificationHandler<ScheduledNotification> {
    @Autowired
    private final LoggingService logger;

    public NumeroUnoNotificationHandler(LoggingService logger) {
        this.logger = logger;
    }

    @Override
    @Async
    public void handle(ScheduledNotification scheduledNotification) {
        logger.logActivity("Test1", "Test 1");
    }
}
