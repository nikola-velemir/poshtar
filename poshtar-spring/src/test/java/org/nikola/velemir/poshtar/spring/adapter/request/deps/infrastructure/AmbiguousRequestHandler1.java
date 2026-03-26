package org.nikola.velemir.poshtar.spring.adapter.request.deps.infrastructure;


import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

//@Handler
public class AmbiguousRequestHandler1 implements RequestHandler<AmbiguousRequest, Unit> {

    @Override
    public Unit handle(AmbiguousRequest ambiguousRequest) {
        return Unit.Value;
    }
}
