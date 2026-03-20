package org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Handler
public class TransactionalRequestHandler implements RequestHandler<TransactionalRequest, Unit> {
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
