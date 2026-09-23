package test.fixtures.rules.semantical.responsiblity;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.VoidCommandHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class SingleRequestHandler implements VoidCommandHandler<SingleRequest> {
    @Override
    public Unit handle(SingleRequest request) {
        return null;
    }
}
