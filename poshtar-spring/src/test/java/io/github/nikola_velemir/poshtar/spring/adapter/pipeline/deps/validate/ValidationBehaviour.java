package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.validate;


import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;

@Behaviour
public class ValidationBehaviour implements PipelineBehaviour<ValidationRequest, Integer> {
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
