package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.success;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import jakarta.persistence.EntityManager;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Handler
public class TransactionalRequestHandler implements RequestHandler<TransactionalRequest, Unit> {
    private final Provider<EntityManager> entityManagerProvider;
    @Inject
    public TransactionalRequestHandler(Provider<EntityManager> provider) {
        entityManagerProvider = provider;
    }
    @Override
    @Transactional
    public Unit handle(TransactionalRequest request) {

        EntityManager em = entityManagerProvider.get();

        // Checking the status
        boolean isActive = em.getTransaction().isActive();

        System.out.println(">>> Is Transaction Active? " + isActive);
        assertTrue(isActive);

        request.payload += 1;
        return Unit.Value;
    }
}
