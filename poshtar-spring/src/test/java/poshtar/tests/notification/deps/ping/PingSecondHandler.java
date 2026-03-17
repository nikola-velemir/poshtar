package poshtar.tests.notification.deps.ping;

import org.nikola.velemir.poshtar.core.annotations.NotificationHandler;
import org.nikola.velemir.poshtar.core.notification.handler.INotificationHandler;

@NotificationHandler
public class PingSecondHandler implements INotificationHandler<PingNotification> {
    @Override
    public void handle(PingNotification pingNotification) {
        pingNotification.payload += 1;
    }
}
