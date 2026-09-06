package io.github.nikola.velemir.poshtar.quarkus.it.component.request;

import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.mock.basic.BasicMockRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.mock.hierarchy.HierarchyRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.chaining.base.*;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.chaining.mock.MockChainedFirstRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.chaining.mock.MockChainedFirstRequestHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.chaining.mock.MockChainedSecondRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.chaining.mock.MockChainedSecondRequestHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.infrastructure.notfound.NotFoundRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.injection.DummyLoggingService;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.injection.InjectionRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.injection.InjectionRequestHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.mock.MockRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.mock.MockRequestHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.mock.MockResponse;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.nullRequest.NullRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.nullRequest.NullRequestHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.ping.PingRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.ping.PingRequestHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.transactional.basic.TransactionalRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.transactional.basic.TransactionalRequestHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.transactional.mandatory.MandatoryRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.transactional.mandatory.MandatoryRequestHandler;
import io.github.nikola_velemir.poshtar.core.exceptions.HandlerNotFoundException;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import io.quarkus.arc.Arc;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.junit.jupiter.api.Test;
import jakarta.transaction.TransactionalException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

@QuarkusTest
@OverruleNoInjection
public class RequestTests {


    @InjectSpy
    Poshtar poshtar;

    @Test
    void should_Register_And_Execute_Handler_Automatically() {
        assertTrue(
                Arc.container().instance(PingRequestHandler.class).isAvailable(),
                "Handler bean not registered through @Handler!"
        );

        PingRequest pingRequest = new PingRequest("Hello Poshtar");
        String response = poshtar.send(pingRequest);

        assertEquals("Pong: Hello Poshtar", response, "Wrong response!");
        System.out.println(">>> TEST PASSED: " + response);


        verify(pingRequestHandler, times(1)).handle(eq(pingRequest));
        verify(poshtar, times(1)).send(any());
    }

    @Test
    void should_Pass_With_At_Transactional() {

        TransactionalRequest transactionalRequest = new TransactionalRequest("Hello Poshtar");

        assertDoesNotThrow(() -> {
            String response = poshtar.send(transactionalRequest);
            assert response.equals("Request with Hello Poshtar") : "Response is incorrect";
        });
        verify(transactionalRequestHandler, times(1)).handle(eq(transactionalRequest));
        verify(poshtar, times(1)).send(any());

        System.out.println(">>> TEST PASSED: ");

    }

    @Test
    void should_Fail_For_Mandatory_Propagation() {
        var request = new MandatoryRequest("Payload");
        Exception ex = assertThrowsExactly(TransactionalException.class, () -> poshtar.send(request));
        assertInstanceOf(TransactionalException.class, ex);
        String expectedMessage = "ARJUNA016110: Transaction is required for invocation";
        String actualMessage = ex.getMessage();
        assertEquals(expectedMessage, actualMessage);

        verify(mandatoryRequestHandler, times(1)).handle(eq(request));
        verify(mandatoryRequestHandler, times(1)).handle(any());
        verify(poshtar, times(1)).send(any());


    }

    @Test
    void should_Chain_Accordingly() {
        var request = new ChainingFirstRequest();
        assertDoesNotThrow(() -> {
            ChainedResponse response = poshtar.send(request);
            assertEquals("Hello from second", response.getResponse());
        });
        verify(chainingFirstRequestHandler, times(1)).handle(any(ChainingFirstRequest.class));
        verify(chainingSecondRequestHandler, times(1)).handle(eq(new ChainingSecondRequest(1)));
        verify(poshtar, times(2)).send(any());

    }

    @Test
    void should_stub_in_hierarchy_handler() {

        MockChainedFirstRequest firstRequest = new MockChainedFirstRequest();
        when(mockChainedSecondRequestHandler.handle(any())).thenReturn("TESTEST");

        var response = poshtar.send(firstRequest);

        assertNotNull(response);
        assertEquals("TESTEST", response.payload());

        verify(mockChainedSecondRequestHandler, times(1)).handle(eq(new MockChainedSecondRequest("Hello")));
        verify(mockChainedfirstRequestHandler, times(1)).handle(eq(firstRequest));
        verify(poshtar, times(2)).send(any());
    }

    @Test
    void should_Register_And_Inject_Service() {

        assertTrue(
                Arc.container().instance(InjectionRequestHandler.class).isAvailable(),
                "Handler bean not registered through @Handler!"
        );

        InjectionRequest injectionRequest = new InjectionRequest("Hello Poshtar");
        String response = poshtar.send(injectionRequest);

        assert response.equals("Request with Logged: Hello Poshtar") : "Incorrect response!";
        verify(injectionRequestHandler, times(1)).handle(eq(injectionRequest));
        verify(dummyLoggingService, times(1)).log(any());
        verify(poshtar, times(1)).send(any());
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
        verify(poshtar, times(1)).send(any());

    }

    @Test
    void should_stub_specific_handler() {
        MockRequest firstMockRequest = new MockRequest("Hello Poshtar");
        MockResponse firstStubbedResponse = new MockResponse("Hello");
        MockRequest secondMockRequest = new MockRequest("Hello Author");
        MockResponse stubbedResponse = new MockResponse("Author");

        when(mockRequestHandler.handle(eq(secondMockRequest))).thenReturn(stubbedResponse);
        when(mockRequestHandler.handle(eq(firstMockRequest))).thenReturn(firstStubbedResponse);

        MockResponse response = poshtar.send(firstMockRequest);

        assertNotNull(response);
        assertEquals("Hello", response.response());

        response = poshtar.send(secondMockRequest);

        assertNotNull(response);
        assertEquals("Author", response.response());


        verify(mockRequestHandler, times(1)).handle(eq(firstMockRequest));

        verify(mockRequestHandler, times(1)).handle(eq(secondMockRequest));
        verify(mockRequestHandler, times(2)).handle(any());
        verify(poshtar, times(2)).send(any());

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

        verify(nullRequestHandler, never()).handle(any());
        verify(poshtar, times(1)).send(any());
    }

    @InjectSpy
    InjectionRequestHandler injectionRequestHandler;
    @InjectSpy
    MandatoryRequestHandler mandatoryRequestHandler;
    @InjectSpy
    DummyLoggingService dummyLoggingService;
    @InjectSpy
    PingRequestHandler pingRequestHandler;
    @InjectSpy
    NullRequestHandler nullRequestHandler;
    @InjectSpy
    TransactionalRequestHandler transactionalRequestHandler;
    @InjectSpy
    ChainingFirstRequestHandler chainingFirstRequestHandler;
    @InjectSpy
    ChainingSecondRequestHandler chainingSecondRequestHandler;
    @InjectMock
    MockRequestHandler mockRequestHandler;
    @InjectSpy
    MockChainedFirstRequestHandler mockChainedfirstRequestHandler;
    @InjectMock
    MockChainedSecondRequestHandler mockChainedSecondRequestHandler;
}
