package org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.nikola.velemir.poshtar.spring.adapter.model.TestEntity;
import org.springframework.transaction.annotation.Transactional;

@Behaviour
public class TransactionalPipeline implements PipelineBehaviour<TransactionalRequest, Unit> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Unit handle(TransactionalRequest request, RequestDelegate<TransactionalRequest, Unit> requestDelegate) {
        request.payload += 1;
        var res = requestDelegate.handle(request);
        System.out.println("Called transactional behaviour");
        var te = new TestEntity("From transactional behaviour");
        entityManager.persist(te);
        return res;
    }
}
