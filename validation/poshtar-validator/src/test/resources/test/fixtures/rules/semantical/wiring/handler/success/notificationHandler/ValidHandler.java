package test.fixtures.rules.semantical.wiring.handler.success.notificationHandler;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class ValidHandler implements NotificationHandler<ValidNotification> {
    @Override
    public void handle(ValidNotification validNotification) {

    }
}
