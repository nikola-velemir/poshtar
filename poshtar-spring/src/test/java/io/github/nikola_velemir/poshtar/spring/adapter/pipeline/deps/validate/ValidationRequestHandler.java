package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.validate;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class ValidationRequestHandler implements RequestHandler<ValidationRequest, Integer> {

    @Override
    public Integer handle(ValidationRequest request) {
        return request.payload() + 1;
    }
}
