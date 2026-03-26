package org.nikola.velemir.poshtar.spring.adapter.request.deps.infrastructure;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.success.TransactionalPipeline;
import org.springframework.beans.factory.annotation.Autowired;


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
