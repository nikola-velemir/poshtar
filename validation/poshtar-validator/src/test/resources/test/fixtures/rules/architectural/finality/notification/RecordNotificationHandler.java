package fixtures.rules.architectural.finality.notification;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class RecordNotificationHandler implements NotificationHandler<RecordNotification> {
    @Override
    public void handle(RecordNotification notification) {

    }
}
