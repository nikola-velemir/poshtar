package poshtar.tests.notification.deps.transactional;

import org.example.core.annotations.NotificationHandler;
import org.example.core.notification.handler.INotificationHandler;
import org.springframework.transaction.annotation.Transactional;

@NotificationHandler
public class TransactionalNotificationFirstHandler implements INotificationHandler<TransactionalNotification> {
    @Override
    @Transactional
    public void handle(TransactionalNotification transactionalNotification) {

    }
}
