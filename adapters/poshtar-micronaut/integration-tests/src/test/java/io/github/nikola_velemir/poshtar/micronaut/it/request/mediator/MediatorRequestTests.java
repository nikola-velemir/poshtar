/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.micronaut.it.request.mediator;

import io.github.nikola_velemir.poshtar.core.exceptions.HandlerNotFoundException;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import io.github.nikola_velemir.poshtar.micronaut.adapter.runtime.internal.mediator.MicronautPoshtar;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.chaining.base.*;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.chaining.mock.MockChainedFirstRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.chaining.mock.MockChainedFirstRequestHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.chaining.mock.MockChainedSecondRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.chaining.mock.MockChainedSecondRequestHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.infrastructure.notfound.NotFoundRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.injection.DummyLoggingService;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.injection.InjectionRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.injection.InjectionRequestHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.mock.MockRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.mock.MockRequestHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.mock.MockResponse;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.nullRequest.NullRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.nullRequest.NullRequestHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.ping.PingRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.ping.PingRequestHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.transactional.basic.TransactionalRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.transactional.basic.TransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.transactional.mandatory.MandatoryRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.request.deps.transactional.mandatory.MandatoryRequestHandler;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import io.micronaut.context.annotation.Property;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.exceptions.IllegalTransactionStateException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.*;

@MicronautTest(rebuildContext = true, transactional = false)
@Property(name = "datasources.default.url", value = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
@Property(name = "datasources.default.driver-class-name", value = "org.h2.Driver")
@Property(name = "datasources.default.username", value = "sa")
@Property(name = "datasources.default.password", value = "")
@Property(name = "datasources.default.dialect", value = "H2")
@Property(name = "jpa.default.packages-to-scan", value = "io.github.nikola_velemir.poshtar")
@Property(name = "jpa.default.properties.hibernate.hbm2ddl.auto", value = "update")
@OverruleNoInjection
public class MediatorRequestTests {

    @Inject
    Poshtar poshtar;

    @Inject
    NullRequestHandler nullRequestHandler;

    @Inject
    PingRequestHandler pingRequestHandler;

    @Inject
    InjectionRequestHandler injectionRequestHandler;

    @Inject
    DummyLoggingService dummyLoggingService;

    @Inject
    TransactionalRequestHandler transactionalRequestHandler;

    @Inject
    MandatoryRequestHandler mandatoryRequestHandler;

    @Inject
    ChainingFirstRequestHandler chainingFirstRequestHandler;

    @Inject
    ChainingSecondRequestHandler chainingSecondRequestHandler;

    @Inject
    MockRequestHandler mockRequestHandler;

    @Inject
    MockChainedFirstRequestHandler mockChainedfirstRequestHandler;

    @Inject
    MockChainedSecondRequestHandler mockChainedSecondRequestHandler;

    @Test
    void handles_Null_Send() {
        NullRequest request = null;
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> poshtar.send(request));
        assertInstanceOf(IllegalArgumentException.class, ex);
        String expected = "Request cannot be null";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
        verify(nullRequestHandler, never()).handle(any());
        verify(poshtar, times(1)).send(any());
    }

    @Test
    void should_stub_specific_handler() {
        MockRequest firstMockRequest = new MockRequest("Hello Poshtar");
        MockResponse firstStubbedResponse = new MockResponse("Hello");
        MockRequest secondMockRequest = new MockRequest("Hello Author");
        MockResponse stubbedResponse = new MockResponse("Author");
        doReturn(stubbedResponse).when(mockRequestHandler).handle(eq(secondMockRequest));
        doReturn(stubbedResponse).when(mockRequestHandler).handle(eq(firstMockRequest));

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
    void should_stub_in_hierarchy_handler() {
        MockChainedFirstRequest firstRequest = new MockChainedFirstRequest();
        when(mockChainedSecondRequestHandler.handle(any())).thenReturn("TESTEST");

        var response = poshtar.send(firstRequest);

        assertNotNull(response);
        assertEquals("TESTEST", response.payload());


        verify(mockChainedSecondRequestHandler, times(1)).handle(eq(new MockChainedSecondRequest("Hello")));
        verify(mockChainedfirstRequestHandler, times(1)).handle(eq(firstRequest));
        //TODO Check on provider
        verify(poshtar, times(2)).send(any());
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
    void should_Pass_With_At_Transactional() {
        TransactionalRequest transactionalRequest = new TransactionalRequest("Hello Poshtar");

        assertDoesNotThrow(() -> {
            String response = poshtar.send(transactionalRequest);
            assertEquals("Request with Hello Poshtar", response, "Response is incorrect");
        });
        verify(transactionalRequestHandler, times(1)).handle(eq(transactionalRequest));
        verify(poshtar, times(1)).send(any());

        System.out.println(">>> TEST PASSED: ");
    }

    @Test
    void should_Fail_For_Mandatory_Propagation() {
        var request = new MandatoryRequest("Payload");
        Exception ex = assertThrows(IllegalTransactionStateException.class, () -> poshtar.send(request));
        assertInstanceOf(IllegalTransactionStateException.class, ex);

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
        //TODO Check on provider
        verify(poshtar, times(2)).send(any());
    }

    @Test
    void should_Register_And_Execute_Handler_Automatically() {

        PingRequest pingRequest = new PingRequest("Hello Poshtar");
        String response = poshtar.send(pingRequest);

        assertEquals("Pong: Hello Poshtar", response, "Wrong response!");
        System.out.println(">>> TEST PASSED: " + response);

        verify(pingRequestHandler, times(1)).handle(eq(pingRequest));
        verify(poshtar, times(1)).send(any());
    }

    @Test
    void should_Register_And_Inject_Service() {

        InjectionRequest injectionRequest = new InjectionRequest("Hello Poshtar");
        String response = poshtar.send(injectionRequest);

        assertEquals("Request with Logged: Hello Poshtar", response, "Incorrect response!");
        verify(injectionRequestHandler, times(1)).handle(eq(injectionRequest));
        verify(dummyLoggingService, times(1)).log(any());
        verify(poshtar, times(1)).send(any());
        System.out.println(">>> TEST PASSED: " + response);
    }

    @MockBean(Poshtar.class)
    Poshtar mockPoshtar(RequestRegistry requestRegistry, NotificationRegistry notificationRegistry, @Named(TaskExecutors.IO) ExecutorService executorService ) {
        return mock(Poshtar.class, delegatesTo(new MicronautPoshtar(requestRegistry, notificationRegistry, executorService)));
    }

    @MockBean(NullRequestHandler.class)
    NullRequestHandler nullRequestHandlerSpy() {
        return mock(NullRequestHandler.class, delegatesTo(new NullRequestHandler()));
    }

    @MockBean(PingRequestHandler.class)
    PingRequestHandler pingRequestHandlerSpy() {
        return mock(PingRequestHandler.class, delegatesTo(new PingRequestHandler()));
    }

    @MockBean(DummyLoggingService.class)
    DummyLoggingService dummyLoggingServiceSpy() {
        return mock(DummyLoggingService.class, delegatesTo(new DummyLoggingService()));
    }

    @MockBean(InjectionRequestHandler.class)
    InjectionRequestHandler injectionRequestHandlerSpy(DummyLoggingService dummyLoggingService) {
        return mock(InjectionRequestHandler.class,
                delegatesTo(new InjectionRequestHandler(dummyLoggingService)));
    }

    @MockBean(ChainingSecondRequestHandler.class)
    ChainingSecondRequestHandler chainingSecondRequestHandlerSpy() {
        return mock(ChainingSecondRequestHandler.class, delegatesTo(new ChainingSecondRequestHandler()));
    }

    @MockBean(ChainingFirstRequestHandler.class)
    ChainingFirstRequestHandler chainingFirstRequestHandlerSpy(Provider<Poshtar> poshtarProvider) {
        return mock(ChainingFirstRequestHandler.class,
                delegatesTo(new ChainingFirstRequestHandler(poshtarProvider)));
    }

    @MockBean(MockChainedFirstRequestHandler.class)
    MockChainedFirstRequestHandler mockChainedfirstRequestHandlerSpy(Provider<Poshtar> poshtarProvider) {
        return mock(MockChainedFirstRequestHandler.class,
                delegatesTo(new MockChainedFirstRequestHandler(poshtarProvider)));
    }

    @MockBean(MockRequestHandler.class)
    MockRequestHandler mockRequestHandler() {
        return mock(MockRequestHandler.class); // No target parameter!
    }

    @MockBean(MockChainedSecondRequestHandler.class)
    MockChainedSecondRequestHandler mockChainedSecondRequestHandler() {
        return mock(MockChainedSecondRequestHandler.class); // No target parameter!
    }

    @MockBean(TransactionalRequestHandler.class)
    TransactionalRequestHandler mockTransactionalHandler() {
        return mock(TransactionalRequestHandler.class, delegatesTo(new TransactionalRequestHandler())); // No target parameter!
    }
}
