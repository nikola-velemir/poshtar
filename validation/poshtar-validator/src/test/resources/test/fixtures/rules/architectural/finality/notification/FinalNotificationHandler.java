package fixtures.rules.architectural.finality.notification;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class FinalNotificationHandler implements NotificationHandler<FinalNotification> {
    @Override
    public void handle(FinalNotification notification) {

    }
}
