package poshtar.tests.pipeline.deps.transactional.mandatory.success;

import org.nikola.velemir.poshtar.core.annotations.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.IPipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@PipelineBehaviour
public class SucceedForMandatoryPipeline implements IPipelineBehaviour<SucceedForMandatoryRequest, Unit> {

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Unit handle(SucceedForMandatoryRequest request, RequestDelegate<SucceedForMandatoryRequest, Unit> requestDelegate) {
        System.out.println("Called inside mandatory transaction");
        boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("Is Transaction REALLY Active? " + isActive);
        return requestDelegate.handle(request);
    }
}
