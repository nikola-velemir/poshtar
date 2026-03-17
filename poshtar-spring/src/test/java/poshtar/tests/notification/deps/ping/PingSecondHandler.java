package poshtar.tests.notification.deps.ping;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class PingSecondHandler implements NotificationHandler<PingNotification> {
    @Override
    public void handle(PingNotification pingNotification) {
        pingNotification.payload += 1;
    }
}
