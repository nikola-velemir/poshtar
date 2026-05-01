package io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.transactional.fail;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import jakarta.persistence.EntityManager;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.model.TestEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Handler
public class FailTransactionalNotificationSecondHandler implements NotificationHandler<FailTransactionalNotification> {
    private final Provider<EntityManager> entityManagerProvider;
    @Inject
    public FailTransactionalNotificationSecondHandler(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }
    @Override
    @Transactional
    public void handle(FailTransactionalNotification transactionalNotification) {
        EntityManager em = entityManagerProvider.get();

        // Checking the status
        boolean isActive = em.getTransaction().isActive();

        System.out.println(">>> Is Transaction Active? " + isActive);
        assertTrue(isActive);

        TestEntity entity = new TestEntity("First " + transactionalNotification.payload());
        em.persist(entity);

        throw new RuntimeException("Failed to persist an entity!");

    }
}
