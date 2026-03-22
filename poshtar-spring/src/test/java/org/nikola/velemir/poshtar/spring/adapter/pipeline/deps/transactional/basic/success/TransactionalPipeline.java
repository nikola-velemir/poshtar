package org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.success;

import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.nikola.velemir.poshtar.spring.adapter.model.TestEntity;
import org.nikola.velemir.poshtar.spring.adapter.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Behaviour
public class TransactionalPipeline implements PipelineBehaviour<TransactionalRequest, Unit> {

    @Autowired
    private final TestRepository repository;

    public TransactionalPipeline(TestRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Unit handle(TransactionalRequest request, RequestDelegate<TransactionalRequest, Unit> requestDelegate) {
        request.payload += 1;
        boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("Is Transaction REALLY Active? " + isActive);
        var res = requestDelegate.handle(request);
        System.out.println("Called transactional behaviour");
        var te = new TestEntity("From transactional behaviour");
        repository.saveAndFlush(te);
        return res;
    }
}
