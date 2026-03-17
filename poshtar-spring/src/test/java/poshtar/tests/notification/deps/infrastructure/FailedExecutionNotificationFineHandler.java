package poshtar.tests.notification.deps.infrastructure;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class FailedExecutionNotificationFineHandler implements NotificationHandler<FailedExecutionNotification> {
    @Override
    public void handle(FailedExecutionNotification failedExecutionNotification) {
        failedExecutionNotification.payload += 1;
    }
}
