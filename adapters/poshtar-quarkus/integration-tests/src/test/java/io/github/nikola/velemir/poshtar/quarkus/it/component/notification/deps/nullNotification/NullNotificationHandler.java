package io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.nullNotification;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class NullNotificationHandler implements NotificationHandler<NullNotification> {
    @Override
    public void handle(NullNotification nullNotification) {

    }
}
