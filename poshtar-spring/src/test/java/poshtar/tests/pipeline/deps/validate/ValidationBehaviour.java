package poshtar.tests.pipeline.deps.validate;


import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.IPipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;

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
