package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.infrastructure.ambiguity;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;


@Handler
public class AmbiguousRequestHandler2 implements RequestHandler<AmbiguousRequest, Unit> {
//    @Autowired
//    private final TransactionalPipeline rq;
//
//    public AmbiguousRequestHandler2(TransactionalPipeline rq) {
//        this.rq = rq;
//    }

    @Override
    public Unit handle(AmbiguousRequest ambiguousRequest) {
        return Unit.Value;
    }
}
