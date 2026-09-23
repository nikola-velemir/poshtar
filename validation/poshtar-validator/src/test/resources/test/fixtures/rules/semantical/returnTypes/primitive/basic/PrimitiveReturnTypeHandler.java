package test.fixtures.rules.semantical.returnTypes.primitive;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.QueryHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class PrimitiveReturnTypeHandler implements QueryHandler<PrimitiveReturnTypeRequest, String> {
    @Override
    public String handle(PrimitiveReturnTypeRequest request) {
        return "";
    }
}
