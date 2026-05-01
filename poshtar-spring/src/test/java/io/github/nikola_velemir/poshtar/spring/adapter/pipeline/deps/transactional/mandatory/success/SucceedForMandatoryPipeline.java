package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.success;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Behaviour
public class SucceedForMandatoryPipeline implements PipelineBehaviour<SucceedForMandatoryRequest, Unit> {

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Unit handle(SucceedForMandatoryRequest request, RequestDelegate<SucceedForMandatoryRequest, Unit> requestDelegate) {
        System.out.println("Called inside mandatory transaction");
        boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("Is Transaction REALLY Active? " + isActive);
        return requestDelegate.handle(request);
    }
}
