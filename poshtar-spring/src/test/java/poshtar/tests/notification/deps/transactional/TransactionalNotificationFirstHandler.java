package poshtar.tests.notification.deps.transactional;

import org.example.core.annotations.NotificationHandler;
import org.example.core.notification.handler.INotificationHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@NotificationHandler
public class TransactionalNotificationFirstHandler implements INotificationHandler<TransactionalNotification> {
    @Override
    @Transactional
    public void handle(TransactionalNotification transactionalNotification) {
        boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("Is Transaction REALLY Active? " + isActive);
        assertTrue(isActive);
    }
}
