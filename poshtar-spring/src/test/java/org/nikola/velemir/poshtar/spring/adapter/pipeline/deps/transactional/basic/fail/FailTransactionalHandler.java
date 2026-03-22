package org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.fail;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@Handler
public class FailTransactionalHandler implements RequestHandler<FailTransactionalRequest, Unit> {
    @Override
    public Unit handle(FailTransactionalRequest request) {
        return null;
    }
}