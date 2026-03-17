package poshtar.tests.notification.deps.infrastructure;

import org.nikola.velemir.poshtar.core.annotations.NotificationHandler;
import org.nikola.velemir.poshtar.core.notification.handler.INotificationHandler;

@NotificationHandler
public class FailedExecutionNotificationFineHandler implements INotificationHandler<FailedExecutionNotification> {
    @Override
    public void handle(FailedExecutionNotification failedExecutionNotification) {
        failedExecutionNotification.payload += 1;
    }
}
