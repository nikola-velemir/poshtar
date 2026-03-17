package com.example.demo.infra.scheduler.handler;

import com.example.demo.shared.logger.LoggingService;
import com.example.demo.infra.scheduler.notification.ScheduledNotification;
import org.nikola.velemir.poshtar.core.annotations.NotificationHandler;
import org.nikola.velemir.poshtar.core.notification.handler.INotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

@NotificationHandler
public class NumeroDosNotificationHandler implements INotificationHandler<ScheduledNotification> {
    @Autowired
    private final LoggingService logger;

    public NumeroDosNotificationHandler(LoggingService logger) {
        this.logger = logger;
    }

    @Override
    @Async
    public void handle(ScheduledNotification scheduledNotification) {
        logger.logActivity("Test2","Test 2");
    }
}