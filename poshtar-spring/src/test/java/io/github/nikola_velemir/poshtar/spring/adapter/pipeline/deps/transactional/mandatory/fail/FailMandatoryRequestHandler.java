package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.fail;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class FailMandatoryRequestHandler implements RequestHandler<FailMandatoryRequest, Unit> {
    @Override
    public Unit handle(FailMandatoryRequest request) {
        request.payload += 1;
        return Unit.Value;
    }
}
