package poshtar.tests.pipeline.deps.validate;

import org.example.core.annotations.RequestHandler;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.types.Unit;

@RequestHandler
public class ValidationBehaviour implements IPipelineBehaviour<ValidationRequest, Integer> {
    @Override
    public Integer handle(ValidationRequest request, RequestDelegate<ValidationRequest, Integer> delegate) {
        System.out.println("Entered validation behaviour!");
        validate(request.payload());
        System.out.println("Passed validation!");
        return delegate.handle(request);
    }

    private void validate(int inputPayload) {
        if (inputPayload == 0) {
            throw new IllegalArgumentException("Payload is wrong");
        }
    }
}
