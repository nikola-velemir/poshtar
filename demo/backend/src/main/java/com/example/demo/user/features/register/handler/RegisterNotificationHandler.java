package com.example.demo.user.features.register.handler;

import com.example.demo.infra.scheduler.logger.LoggingService;
import com.example.demo.infra.service.EmailService;
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

    private final EmailService emailService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void handle(RegisterNotification notification) {
        String username = notification.username();
        String email = notification.email();
        emailService.sendEmail(email, "Activate account", "http://localhost:4200/activate/" + username);
    }
}
