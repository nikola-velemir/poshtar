package poshtar.tests.notification.deps.async;

import org.example.core.annotations.NotificationHandler;
import org.example.core.notification.handler.INotificationHandler;
import org.springframework.scheduling.annotation.Async;

@NotificationHandler
public class FailForAsyncSecondHandler implements INotificationHandler<FailForAsyncNotification> {
    @Override
    @Async
    public void handle(FailForAsyncNotification failForAsyncNotification) {
        System.out.println("Second handler called!");
    }
}
