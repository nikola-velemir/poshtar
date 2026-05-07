package io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.transactional.basic;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Handler
public class TransactionalNotificationFirstHandler implements NotificationHandler<TransactionalNotification> {
    @Override
    @Transactional
    public void handle(TransactionalNotification transactionalNotification) {
        boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("Is Transaction REALLY Active? " + isActive);
        assertTrue(isActive);
    }
}
