package test.fixtures.rules.architectural.noInjection.handler;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class InjectedNotificationHandler implements NotificationHandler<InjectedNotification> {
    @Override
    public void handle(InjectedNotification notification) {

    }
}
