package test.fixtures.rules.semantical.returnTypes.wrapsPrimitives;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.QueryHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

import java.util.List;

@Handler
public class WrapsPrimitivesHandler implements QueryHandler<WrapsPrimitivesRequest, List<String>> {
    @Override
    public List<String> handle(WrapsPrimitivesRequest request) {
        return List.of();
    }
}
