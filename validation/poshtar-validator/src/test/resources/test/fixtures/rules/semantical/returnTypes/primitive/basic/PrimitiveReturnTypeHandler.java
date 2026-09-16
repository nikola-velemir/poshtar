package test.fixtures.rules.semantical.returnTypes.primitive;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class PrimitiveReturnTypeHandler implements RequestHandler<PrimitiveReturnTypeRequest, String> {
    @Override
    public String handle(PrimitiveReturnTypeRequest request) {
        return "";
    }
}
