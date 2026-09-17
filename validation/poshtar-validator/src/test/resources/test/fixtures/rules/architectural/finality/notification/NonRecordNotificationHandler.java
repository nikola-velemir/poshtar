package fixtures.rules.architectural.finality.notification;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class NonRecordNotificationHandler implements NotificationHandler<NonRecordNotification> {
    @Override
    public void handle(NonRecordNotification notification) {

    }
}
