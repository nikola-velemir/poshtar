package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.fail;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class FailTransactionalRequestHandler implements RequestHandler<FailTransactionalRequest, Unit> {
    @Override
    public Unit handle(FailTransactionalRequest request) {
        return null;
    }
}
