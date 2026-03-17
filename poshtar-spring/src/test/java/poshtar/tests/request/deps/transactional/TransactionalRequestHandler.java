package poshtar.tests.request.deps.transactional;

import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RequestHandler
public class TransactionalRequestHandler implements IRequestHandler<TransactionalRequest, String> {

    public TransactionalRequestHandler() {
    }

    @Transactional
    @Override
    public String handle(TransactionalRequest injectionRequest) {

        boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("Is Transaction REALLY Active? " + isActive);
        assertTrue(isActive);
        return "Request with " + injectionRequest.payload();
    }
}
