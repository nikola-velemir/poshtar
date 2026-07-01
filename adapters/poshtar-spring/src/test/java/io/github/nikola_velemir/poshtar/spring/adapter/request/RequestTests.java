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

package io.github.nikola_velemir.poshtar.spring.adapter.request;

import io.github.nikola_velemir.poshtar.core.exceptions.HandlerNotFoundException;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.chaining.base.*;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.chaining.mock.MockChainedFirstRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.chaining.mock.MockChainedFirstRequestHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.chaining.mock.MockChainedSecondRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.chaining.mock.MockChainedSecondRequestHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.injection.DummyLoggingService;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.mock.MockRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.mock.MockRequestHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.mock.MockResponse;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.nullRequest.NullRequestHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.transactional.mandatory.MandatoryRequestHandler;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.IllegalTransactionStateException;
import io.github.nikola_velemir.poshtar.spring.adapter.MockTransactionConfig;
import io.github.nikola_velemir.poshtar.spring.adapter.TestApplication;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.infrastructure.notfound.NotFoundRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.injection.InjectionRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.injection.InjectionRequestHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.nullRequest.NullRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.ping.PingRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.ping.PingRequestHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.transactional.mandatory.MandatoryRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.transactional.basic.TransactionalRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.transactional.basic.TransactionalRequestHandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TestApplication.class)
@Import(MockTransactionConfig.class)
@OverruleNoInjection
public class RequestTests {
    @Autowired
    @MockitoSpyBean
    private Poshtar poshtar;
    @Autowired
    private ApplicationContext context;

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
        boolean beanExists = context.containsBean(TransactionalRequestHandler.class.getName());
        assert beanExists : "Handler bean has not been registered thru @RequestHandler!";
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
        Exception ex = assertThrowsExactly(IllegalTransactionStateException.class, () -> poshtar.send(request));
        assertInstanceOf(IllegalTransactionStateException.class, ex);
        String expectedMessage = "No existing transaction found for transaction marked with propagation 'mandatory'";
        String actualMessage = ex.getMessage();
        assertEquals(expectedMessage, actualMessage);

        verify(mandatoryRequestHandler, never()).handle(eq(request));
        verify(mandatoryRequestHandler, never()).handle(any());
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
    void should_Register_And_Execute_Handler_Automatically() {

        boolean beanExists = context.containsBean(PingRequestHandler.class.getName());
        assert beanExists : "Handler bean not registered thru @RequestHandler!";

        PingRequest pingRequest = new PingRequest("Hello Poshtar");
        String response = poshtar.send(pingRequest);

        assert response.equals("Pong: Hello Poshtar") : "Wrong response!";
        System.out.println(">>> TEST PASSED: " + response);

        verify(pingRequestHandler, times(1)).handle(eq(pingRequest));
        verify(poshtar, times(1)).send(any());

    }

    @Test
    void should_Register_And_Inject_Service() {

        boolean beanExists = context.containsBean(InjectionRequestHandler.class.getName());
        assert beanExists : "Handler not registered thru @RequestHandler!";

        InjectionRequest injectionRequest = new InjectionRequest("Hello Poshtar");
        String response = poshtar.send(injectionRequest);

        assert response.equals("Request with Logged: Hello Poshtar") : "Incorrect response!";
        System.out.println(">>> TEST PASSED: " + response);
        verify(injectionRequestHandler, times(1)).handle(eq(injectionRequest));
        verify(dummyLoggingService, times(1)).log(any());
        verify(poshtar, times(1)).send(any());

    }

    @MockitoSpyBean
    private NullRequestHandler nullRequestHandler;
    @MockitoSpyBean
    private PingRequestHandler pingRequestHandler;
    @MockitoSpyBean
    private InjectionRequestHandler injectionRequestHandler;
    @MockitoSpyBean
    private DummyLoggingService dummyLoggingService;
    @MockitoSpyBean
    private TransactionalRequestHandler transactionalRequestHandler;
    @MockitoSpyBean
    private MandatoryRequestHandler mandatoryRequestHandler;
    @MockitoSpyBean
    private ChainingFirstRequestHandler chainingFirstRequestHandler;
    @MockitoSpyBean
    private ChainingSecondRequestHandler chainingSecondRequestHandler;
    @MockitoBean
    private MockRequestHandler mockRequestHandler;
    @MockitoSpyBean
    private MockChainedFirstRequestHandler mockChainedfirstRequestHandler;
    @MockitoBean
    private MockChainedSecondRequestHandler mockChainedSecondRequestHandler;
}
