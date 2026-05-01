package io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.infrastructure;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
@Handler
public class FailedExecutionNotificationHandler implements NotificationHandler<FailedExecutionNotification> {
    @Override
    public void handle(FailedExecutionNotification failedExecutionNotification) {
        throw new RuntimeException("Purposefully failed the execution!");
    }
}
