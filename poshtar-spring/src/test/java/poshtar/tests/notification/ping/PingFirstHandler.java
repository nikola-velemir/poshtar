package poshtar.tests.notification.ping;

import org.example.core.annotations.NotificationHandler;
import org.example.core.notification.handler.INotificationHandler;
import org.example.core.types.Unit;

@NotificationHandler
public class PingFirstHandler implements INotificationHandler<PingNotification> {

    @Override
    public void handle(PingNotification pingNotification) {
        pingNotification.payload += 1;
    }
}
