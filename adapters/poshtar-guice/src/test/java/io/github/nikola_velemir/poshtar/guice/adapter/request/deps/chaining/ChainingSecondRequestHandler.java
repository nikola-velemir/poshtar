package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Handler
public class ChainingSecondRequestHandler implements RequestHandler<ChainingSecondRequest, String> {
    @Override
    public String handle(ChainingSecondRequest chainingSecondRequest) {
        System.out.println("Executing Second Handler Hashcode: " + System.identityHashCode(this));

        assertEquals(1, chainingSecondRequest.id);
        return "Hello from second";
    }
}
