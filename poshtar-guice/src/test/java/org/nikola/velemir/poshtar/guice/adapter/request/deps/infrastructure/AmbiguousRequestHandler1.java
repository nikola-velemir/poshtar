package org.nikola.velemir.poshtar.guice.adapter.request.deps.infrastructure;


import jakarta.inject.Inject;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.nikola.velemir.poshtar.guice.adapter.request.deps.injection.DummyLoggingService;
import org.nikola.velemir.poshtar.guice.adapter.request.deps.transactional.success.TransactionalRequestHandler;

@Handler
public class AmbiguousRequestHandler1 implements RequestHandler<AmbiguousRequest, Unit> {
//    private final TransactionalRequestHandler loggingService;
//
//    @Inject
//    public AmbiguousRequestHandler1(TransactionalRequestHandler loggingService) {
//        this.loggingService = loggingService;
//    }

    @Override
    public Unit handle(AmbiguousRequest ambiguousRequest) {
        return Unit.Value;
    }
}
