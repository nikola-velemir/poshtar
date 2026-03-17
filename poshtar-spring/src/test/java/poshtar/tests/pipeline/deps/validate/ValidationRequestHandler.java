package poshtar.tests.pipeline.deps.validate;


import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;

@RequestHandler
public class ValidationRequestHandler implements IRequestHandler<ValidationRequest, Integer> {

    @Override
    public Integer handle(ValidationRequest request) {
        return request.payload() + 1;
    }
}
