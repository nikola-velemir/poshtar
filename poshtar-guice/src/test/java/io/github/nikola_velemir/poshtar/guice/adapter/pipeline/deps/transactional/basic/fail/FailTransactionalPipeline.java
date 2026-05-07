package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.fail;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import jakarta.persistence.EntityManager;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import io.github.nikola_velemir.poshtar.guice.adapter.model.TestEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Behaviour
public class FailTransactionalPipeline  implements PipelineBehaviour<FailTransactionalRequest, Unit> {
    private final Provider<EntityManager> entityManagerProvider;
    @Inject
    public FailTransactionalPipeline(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    @Transactional
    public Unit handle(FailTransactionalRequest request, RequestDelegate<FailTransactionalRequest, Unit> delegate) {
        EntityManager em = entityManagerProvider.get();

        // Checking the status
        boolean isActive = em.getTransaction().isActive();

        System.out.println(">>> Is Transaction Active? " + isActive);
        assertTrue(isActive);

        System.out.println("Called failure transactional pipeline!");


        var te = new TestEntity(request.payload());
        em.persist(te);

        throw new RuntimeException("Purposefully failing the pipeline!");
    }
}
