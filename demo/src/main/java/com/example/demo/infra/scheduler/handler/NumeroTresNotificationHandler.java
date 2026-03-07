package com.example.demo.infra.scheduler.handler;

import com.example.demo.infra.scheduler.logger.LoggingService;
import com.example.demo.infra.scheduler.notification.ScheduledNotification;
import org.example.core.annotations.NotificationHandler;
import org.example.core.notification.handler.INotificationHandler;
import org.example.core.types.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

@NotificationHandler
public class NumeroTresNotificationHandler implements INotificationHandler<ScheduledNotification> {
    @Autowired
    private final LoggingService logger;

    public NumeroTresNotificationHandler(LoggingService logger) {
        this.logger = logger;
    }

    @Override
    @Async
    public void handle(ScheduledNotification scheduledNotification) {
        logger.logActivity("Test3","Test 3");
    }
}
