package com.example.demo.infra.scheduler.handler;

import com.example.demo.shared.logger.LoggingService;
import com.example.demo.infra.scheduler.notification.ScheduledNotification;

import org.nikola.velemir.poshtar.core.annotations.NotificationHandler;
import org.nikola.velemir.poshtar.core.notification.handler.INotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@NotificationHandler
public class NumeroTresNotificationHandler implements INotificationHandler<ScheduledNotification> {
    @Autowired
    private final LoggingService logger;

    public NumeroTresNotificationHandler(LoggingService logger) {
        this.logger = logger;
    }

    @Override

    @Async
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(ScheduledNotification scheduledNotification) {
        logger.logActivity("Test3","Test 3");
    }
}
