package com.example.demo.user.features.register.handler;

import com.example.demo.shared.logger.LoggingService;
import com.example.demo.infra.service.EmailService;
import com.example.demo.user.features.register.notification.RegisterNotification;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Handler
public class RegisterNotificationHandler implements NotificationHandler<RegisterNotification> {

    private final EmailService emailService;
    private final LoggingService loggingService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void handle(RegisterNotification notification) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String username = notification.username();
        String email = notification.email();

        emailService.sendEmail(email, "Activate account", "http://localhost:4200/activate/" + username);
        loggingService.logActivity("EMAIL SENT", "email sent to " + email);
    }
}
