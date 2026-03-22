package org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.success;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import jakarta.persistence.EntityManager;
import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.nikola.velemir.poshtar.guice.adapter.model.TestEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Behaviour
public class TransactionalPipeline implements PipelineBehaviour<TransactionalRequest, Unit> {
    private final Provider<EntityManager> emProvider;

    @Inject
    public TransactionalPipeline(Provider<EntityManager> emProvider) {
        this.emProvider = emProvider;
    }

    @Override
    @Transactional
    public Unit handle(TransactionalRequest request, RequestDelegate<TransactionalRequest, Unit> requestDelegate) {
        request.payload += 1;
        var res = requestDelegate.handle(request);
        System.out.println("Called transactional behaviour");
        EntityManager em = emProvider.get();

        // Checking the status
        boolean isActive = em.getTransaction().isActive();

        System.out.println(">>> Is Transaction Active? " + isActive);
        assertTrue(isActive);

        TestEntity entity = new TestEntity("From transactional pipeline");
        em.persist(entity);
        return res;
    }
}
