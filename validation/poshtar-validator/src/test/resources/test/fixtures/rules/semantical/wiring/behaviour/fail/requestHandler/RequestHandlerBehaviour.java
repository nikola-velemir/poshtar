package test.fixtures.rules.semantical.wiring.fail.requestHandler;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class RequestHandlerBehaviour implements RequestHandler<RequestHandlerRequest, Unit> {
    @Override
    public Unit handle(RequestHandlerRequest requestHandlerRequest) {
        return null;
    }
}
