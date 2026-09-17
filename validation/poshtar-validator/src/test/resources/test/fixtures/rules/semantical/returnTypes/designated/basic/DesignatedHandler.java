package test.fixtures.rules.semantical.returnTypes;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class DesignatedHandler implements RequestHandler<DesignatedRequest, DesignatedResponse> {
    @Override
    public DesignatedResponse handle(DesignatedRequest request) {
        return null;
    }
}
