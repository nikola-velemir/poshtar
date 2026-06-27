package io.github.nikola.velemir.poshtar.quarkus.it.component.request;

import io.github.nikola.velemir.poshtar.quarkus.it.component.request.ping.PingRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.ping.PingRequestHandler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.quarkus.arc.Arc;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class RequestTests {


    @Inject
    Poshtar poshtar;
    @Test
    void should_Register_And_Execute_Handler_Automatically() {
        assertTrue(
                Arc.container().instance(PingRequestHandler.class).isAvailable(),
                "Handler bean not registered through @Handler!"
        );

        String response = poshtar.send(new PingRequest("Hello Poshtar"));

        assertEquals("Pong: Hello Poshtar", response, "Wrong response!");
        System.out.println(">>> TEST PASSED: " + response);
    }
}
