package test.fixtures.rules.semantical.responsiblity;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class SingleNotificationHandler implements NotificationHandler<SingleNotification> {

    @Override
    public void handle(SingleNotification notification) {

    }
}
