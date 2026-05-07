package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.infrastructure;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

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
