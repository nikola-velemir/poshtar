package com.example.demo.user.features.register.handler;

import com.example.demo.infra.scheduler.logger.LoggingService;
import com.example.demo.user.features.register.notification.RegisterNotification;
import lombok.RequiredArgsConstructor;
import org.example.core.annotations.NotificationHandler;
import org.example.core.notification.handler.INotificationHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@NotificationHandler
public class RegisterNotificationHandler implements INotificationHandler<RegisterNotification> {
    private final LoggingService loggingService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void handle(RegisterNotification registerNotification) {
        String username = registerNotification.username();
        loggingService.logActivity("USER REGISTERED", "User username with name :" + username);
    }
}
