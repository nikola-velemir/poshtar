package poshtar.tests.notification.deps.infrastructure;

import org.example.core.annotations.NotificationHandler;
import org.example.core.notification.handler.INotificationHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@NotificationHandler
public class FailedExecutionNotificationHandler implements INotificationHandler<FailedExecutionNotification> {
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void handle(FailedExecutionNotification failedExecutionNotification) {
    }
}
