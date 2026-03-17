package poshtar.tests.notification.deps.async;

import org.nikola.velemir.poshtar.core.annotations.NotificationHandler;
import org.nikola.velemir.poshtar.core.notification.handler.INotificationHandler;

@NotificationHandler
public class FailForAsyncFirstHandler implements INotificationHandler<FailForAsyncNotification> {
    @Override
    public void handle(FailForAsyncNotification failForAsyncNotification) {
        System.out.println("First handler called!");

    }
}
