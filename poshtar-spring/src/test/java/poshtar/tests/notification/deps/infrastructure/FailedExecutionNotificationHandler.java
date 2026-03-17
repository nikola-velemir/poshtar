package poshtar.tests.notification.deps.infrastructure;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Handler
public class FailedExecutionNotificationHandler implements NotificationHandler<FailedExecutionNotification> {
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void handle(FailedExecutionNotification failedExecutionNotification) {
    }
}
