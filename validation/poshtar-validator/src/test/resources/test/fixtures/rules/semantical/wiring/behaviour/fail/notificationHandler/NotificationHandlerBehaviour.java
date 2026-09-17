package test.fixtures.rules.semantical.wiring.fail.notificationHandler;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

@Behaviour
public class NotificationHandlerBehaviour implements NotificationHandler<NotificationHandlerNotification> {
    @Override
    public void handle(NotificationHandlerNotification notificationHandlerNotification) {
        
    }
}
