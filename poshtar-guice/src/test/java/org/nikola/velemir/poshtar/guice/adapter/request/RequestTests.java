package org.nikola.velemir.poshtar.guice.adapter.request;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nikola.velemir.poshtar.core.exceptions.HandlerNotFoundException;
import org.nikola.velemir.poshtar.core.mediator.Poshtar;
import org.nikola.velemir.poshtar.guice.adapter.TestModule;
import org.nikola.velemir.poshtar.guice.adapter.request.deps.infrastructure.NotFoundRequest;
import org.nikola.velemir.poshtar.guice.adapter.request.deps.injection.InjectionRequest;
import org.nikola.velemir.poshtar.guice.adapter.request.deps.nullRequest.NullRequest;
import org.nikola.velemir.poshtar.guice.adapter.request.deps.ping.PingRequest;

import static org.junit.jupiter.api.Assertions.*;

public class RequestTests {
    private static Poshtar poshtar;

    @BeforeAll
    static void setUp() {
        Injector injector = Guice.createInjector(new TestModule());

        poshtar = injector.getInstance(Poshtar.class);
    }

    @Test
    void should_Register_And_Execute_Handler_Automatically() {

        String response = poshtar.send(new PingRequest("Hello Poshtar"));

        assert response.equals("Pong: Hello Poshtar") : "Wrong response!";
        System.out.println(">>> TEST PASSED: " + response);

    }
    @Test
    void handles_Null_Send() {
        NullRequest request = null;
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> {
            poshtar.send(request);
        });
        assertInstanceOf(IllegalArgumentException.class, ex);
        String expected = "Request cannot be null";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
    }
    @Test
    void should_fail_for_unregistered_handler() {
        NotFoundRequest request = new NotFoundRequest();
        Exception ex = assertThrowsExactly(HandlerNotFoundException.class, () -> {
            poshtar.send(request);
        });
        assertInstanceOf(HandlerNotFoundException.class, ex);
        String expectedMessage = "[PoshtaR] No handler found for type: [NotFoundRequest].";
        String actualMessage = ex.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
    @Test
    void should_Register_And_Inject_Service() {
        String response = poshtar.send(new InjectionRequest("Hello Poshtar"));

        assert response.equals("Request with Logged: Hello Poshtar") : "Incorrect response!";
        System.out.println(">>> TEST PROŠAO: " + response);
    }
}
