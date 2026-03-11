package poshtar.tests.notification.deps.async;

import org.example.core.annotations.NotificationHandler;
import org.example.core.notification.handler.INotificationHandler;

@NotificationHandler
public class FailForAsyncFirstHandler implements INotificationHandler<FailForAsyncNotification> {
    @Override
    public void handle(FailForAsyncNotification failForAsyncNotification) {
        System.out.println("First handler called!");

    }
}
