package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success;
import com.google.inject.Inject;
import com.google.inject.Provider;
import jakarta.persistence.EntityManager;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import com.google.inject.persist.Transactional;
import io.github.nikola_velemir.poshtar.guice.adapter.model.TestEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Handler
public class TransactionalRequestHandler implements RequestHandler<TransactionalRequest, String> {
    private final Provider<EntityManager> entityManagerProvider;
    @Inject
    public TransactionalRequestHandler(Provider<EntityManager> provider) {
        entityManagerProvider = provider;
    }

    @Transactional
    @Override
    public String handle(TransactionalRequest injectionRequest) {

        EntityManager em = entityManagerProvider.get();

        // Checking the status
        boolean isActive = em.getTransaction().isActive();

        System.out.println(">>> Is Transaction Active? " + isActive);
        assertTrue(isActive);

        TestEntity entity = new TestEntity(injectionRequest.payload());
        em.persist(entity);
        return "Request with " + injectionRequest.payload();
    }
}
