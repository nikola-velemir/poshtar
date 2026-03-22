package demo.user.features.register.handler;


import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import demo.infra.service.EmailService;
import demo.logs.service.LoggingService;
import demo.user.features.register.notification.RegisterNotification;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Handler
public class RegisterNotificationHandler implements NotificationHandler<RegisterNotification> {

    private final EmailService emailService;
    private final LoggingService loggingService;

    @Override
    @Transactional()
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
