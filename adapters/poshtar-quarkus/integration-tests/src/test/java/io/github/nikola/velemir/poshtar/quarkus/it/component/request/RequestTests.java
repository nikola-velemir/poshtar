package io.github.nikola.velemir.poshtar.quarkus.it.component.request;

import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.infrastructure.notfound.NotFoundRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.injection.InjectionRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.injection.InjectionRequestHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.nullRequest.NullRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.ping.PingRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.ping.PingRequestHandler;
import io.github.nikola_velemir.poshtar.core.exceptions.HandlerNotFoundException;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.quarkus.arc.Arc;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

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
    @Test
    void should_Register_And_Inject_Service() {

        assertTrue(
                Arc.container().instance(InjectionRequestHandler.class).isAvailable(),
                "Handler bean not registered through @Handler!"
        );

        String response = poshtar.send(new InjectionRequest("Hello Poshtar"));

        assert response.equals("Request with Logged: Hello Poshtar") : "Incorrect response!";
        System.out.println(">>> TEST PASSSED: " + response);
    }
    @Test
    void should_fail_for_unregistered_handler() {
        NotFoundRequest request = new NotFoundRequest();
        Exception ex = assertThrowsExactly(HandlerNotFoundException.class, () -> poshtar.send(request));
        assertInstanceOf(HandlerNotFoundException.class, ex);
        String expectedMessage = "[PoshtaR] No handler found for type: [NotFoundRequest].";
        String actualMessage = ex.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
    @Test
    void handles_Null_Send() {
        System.out.println(RequestHandler.class.getSimpleName());
        NullRequest request = null;
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> poshtar.send(request));
        assertInstanceOf(IllegalArgumentException.class, ex);
        String expected = "Request cannot be null";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
    }
}
