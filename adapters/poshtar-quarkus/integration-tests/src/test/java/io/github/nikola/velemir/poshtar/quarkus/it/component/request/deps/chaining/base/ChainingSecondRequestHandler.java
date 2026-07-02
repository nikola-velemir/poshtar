package io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.chaining.base;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Handler
public class ChainingSecondRequestHandler implements RequestHandler<ChainingSecondRequest, String> {
    @Override
    public String handle(ChainingSecondRequest chainingSecondRequest) {
        assertEquals(1, chainingSecondRequest.id);
        return "Hello from second";
    }
}
