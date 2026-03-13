package poshtar.tests.pipeline.deps.transactional;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RequestHandler
public class TransactionalRequestHandler implements IRequestHandler<TransactionalRequest, Unit> {
    @Override
    @Transactional
    public Unit handle(TransactionalRequest request) {
        boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("Is Transaction REALLY Active? " + isActive);
        assertTrue(isActive);
        request.payload += 1;
        return Unit.Value;
    }
}
