package fixtures.rules.architectural.finality.notification;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class NonFinalNotificationHandler implements NotificationHandler<NonFinalNotification> {
    @Override
    public void handle(NonFinalNotification notification) {

    }
}
