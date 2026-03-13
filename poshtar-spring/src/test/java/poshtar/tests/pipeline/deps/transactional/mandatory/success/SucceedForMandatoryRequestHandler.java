package poshtar.tests.pipeline.deps.transactional.mandatory.success;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RequestHandler
public class SucceedForMandatoryRequestHandler implements IRequestHandler<SucceedForMandatoryRequest, Unit> {
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Unit handle(SucceedForMandatoryRequest request) {
        boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("Is Transaction REALLY Active? " + isActive);
        assertTrue(isActive);

        request.payload += 1;
        return Unit.Value;
    }
}
