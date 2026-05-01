package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.fail;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import jakarta.persistence.EntityManager;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.model.TestEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Handler
public class FailForTransactionalRequestHandler implements RequestHandler<FailForTransactionalRequest, String> {
    private final Provider<EntityManager> entityManagerProvider;
    @Inject
    public FailForTransactionalRequestHandler(Provider<EntityManager> provider) {
        entityManagerProvider = provider;
    }
    @Override
    @Transactional
    public String handle(FailForTransactionalRequest request) {
        EntityManager em = entityManagerProvider.get();

        // Checking the status
        boolean isActive = em.getTransaction().isActive();

        System.out.println(">>> Is Transaction Active? " + isActive);
        assertTrue(isActive);

        TestEntity entity = new TestEntity(request.payload());
        em.persist(entity);
        throw new RuntimeException("Simulated entity persistance failed!");
    }
}
