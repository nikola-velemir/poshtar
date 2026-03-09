package poshtar.tests.notification.deps.infrastructure;

import org.example.core.annotations.NotificationHandler;
import org.example.core.notification.handler.INotificationHandler;

@NotificationHandler
public class FailedExecutionNotificationFineHandler implements INotificationHandler<FailedExecutionNotification> {
    @Override
    public void handle(FailedExecutionNotification failedExecutionNotification) {
        failedExecutionNotification.payload += 1;
    }
}
