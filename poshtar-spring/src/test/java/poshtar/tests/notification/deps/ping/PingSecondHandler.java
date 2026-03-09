package poshtar.tests.notification.deps.ping;

import org.example.core.annotations.NotificationHandler;
import org.example.core.notification.handler.INotificationHandler;

@NotificationHandler
public class PingSecondHandler implements INotificationHandler<PingNotification> {
    @Override
    public void handle(PingNotification pingNotification) {
        pingNotification.payload += 1;
    }
}
