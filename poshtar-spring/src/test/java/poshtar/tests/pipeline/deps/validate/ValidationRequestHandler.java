package poshtar.tests.pipeline.deps.validate;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;

@RequestHandler
public class ValidationRequestHandler implements IRequestHandler<ValidationRequest, Integer> {

    @Override
    public Integer handle(ValidationRequest request) {
        return request.payload() + 1;
    }
}
