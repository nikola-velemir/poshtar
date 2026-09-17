package test.fixtures.rules.semantical.returnTypes.designated.wraps;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

import java.util.List;

@Handler
public class WrapsDesignatedHandler implements RequestHandler<WrapsDesignatedRequest, List<WrapsDesignatedResponse>> {
    @Override
    public List<WrapsDesignatedResponse> handle(WrapsDesignatedRequest request) {
        return List.of();
    }
}
