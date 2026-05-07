package io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.infrastructure;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class FailedExecutionNotificationFineHandler implements NotificationHandler<FailedExecutionNotification> {
    @Override
    public void handle(FailedExecutionNotification failedExecutionNotification) {
        failedExecutionNotification.payload += 1;
    }
}
