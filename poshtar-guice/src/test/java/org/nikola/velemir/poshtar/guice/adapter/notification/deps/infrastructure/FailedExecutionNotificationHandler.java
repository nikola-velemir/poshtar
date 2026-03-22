package org.nikola.velemir.poshtar.guice.adapter.notification.deps.infrastructure;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
@Handler
public class FailedExecutionNotificationHandler implements NotificationHandler<FailedExecutionNotification> {
    @Override
    public void handle(FailedExecutionNotification failedExecutionNotification) {
        throw new RuntimeException("Purposefully failed the execution!");
    }
}
