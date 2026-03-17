package poshtar.tests.pipeline.deps.validate;


import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class ValidationRequestHandler implements RequestHandler<ValidationRequest, Integer> {

    @Override
    public Integer handle(ValidationRequest request) {
        return request.payload() + 1;
    }
}
