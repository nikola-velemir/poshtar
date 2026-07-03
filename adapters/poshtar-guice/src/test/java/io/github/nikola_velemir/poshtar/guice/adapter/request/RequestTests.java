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

package io.github.nikola_velemir.poshtar.guice.adapter.request;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining.ChainingFirstRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining.ChainingFirstRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining.ChainingSecondRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining.ChainingSecondRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.DummyLoggingService;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.InjectionRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.mock.MockRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.mock.MockRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.mock.MockResponse;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.nullRequest.NullRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.ping.PingRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.fail.FailForTransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.TransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.UpdateTransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import io.github.nikola_velemir.poshtar.core.exceptions.HandlerNotFoundException;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.guice.adapter.TestModule;
import io.github.nikola_velemir.poshtar.guice.adapter.model.TestEntity;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.infrastructure.NotFoundRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.InjectionRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.InjectionResponse;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.ping.PingRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.fail.FailForTransactionalRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.TransactionalRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.UpdateTransactionalRequest;
import org.mockito.Mockito;

import java.util.List;

import static io.github.nikola_velemir.poshtar.guice.adapter.request.RequestTestsUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@OverruleNoInjection
public class RequestTests {
    static Poshtar poshtar;
    private Injector injector;
    static NullRequestHandler nullRequestHandler;
    static PingRequestHandler pingRequestHandler;
    static InjectionRequestHandler injectionRequestHandler;
    static TransactionalRequestHandler transactionalRequestHandler;
    static UpdateTransactionalRequestHandler updateTransactionalRequestHandler;
    static FailForTransactionalRequestHandler failForTransactionalRequestHandler;
    static DummyLoggingService dummyLoggingService;
    static ChainingFirstRequestHandler chainingFirstRequestHandler;
    static ChainingSecondRequestHandler chainingSecondRequestHandler;
    static MockRequestHandler mockRequestHandler;


    static {
        java.util.logging.Logger.getLogger("com.google.inject.internal.ProxyFactory")
                .setLevel(java.util.logging.Level.SEVERE);
    }


    @BeforeEach
    void initTestContainer() {
        // 1. Spy the shared dependency first
        createMocks();
        Injector bootstrapInjector = Guice.createInjector(new TestModule());
        DummyLoggingService realLoggingService = bootstrapInjector.getInstance(DummyLoggingService.class);
        dummyLoggingService = Mockito.spy(realLoggingService);

        // 2. Create handlers with spy dependency injected, then wrap them in spies
        Injector handlerInjector = Guice.createInjector(
                Modules.override(new TestModule()).with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(DummyLoggingService.class).toInstance(dummyLoggingService);
                    }
                })
        );

        createSpies(handlerInjector);


        injector = buildTestInjector();

        poshtar = injector.getInstance(Poshtar.class);
    }

    @Test
    @Disabled
    void should_Chain_Accordingly() {
        var request = new ChainingFirstRequest();
        assertDoesNotThrow(() -> {
            var response = poshtar.send(request);
            assertEquals("Hello from second", response.getResponse());
        });
        System.out.println("Spy Handler Hashcode: " + System.identityHashCode(RequestTests.chainingFirstRequestHandler));
        System.out.println("Spy Second Handler Hashcode: " + System.identityHashCode(RequestTests.chainingSecondRequestHandler));

        verify(chainingFirstRequestHandler, times(1)).handle(any(ChainingFirstRequest.class));
        verify(chainingSecondRequestHandler, times(1)).handle(any(ChainingSecondRequest.class));

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

    }

    @Test
    void should_Register_And_Execute_Handler_Automatically() {

        PingRequest pingRequest = new PingRequest("Hello Poshtar");
        String response = poshtar.send(pingRequest);

        assert response.equals("Pong: Hello Poshtar") : "Wrong response!";
        System.out.println(">>> TEST PASSED: " + response);
        verify(pingRequestHandler, times(1)).handle(eq(pingRequest));

    }

    @Test
    void handles_Null_Send() {
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> poshtar.send(null));
        assertInstanceOf(IllegalArgumentException.class, ex);
        String expected = "Request cannot be null";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
        verify(nullRequestHandler, never()).handle(any());
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
    void should_Register_And_Inject_Service() {
        var injectionRequest = new InjectionRequest("Hello Poshtar");
        InjectionResponse response = poshtar.send(injectionRequest);

        assert response.payload().equals("Request with Logged: Hello Poshtar") : "Incorrect response!";
        verify(injectionRequestHandler, times(1)).handle(eq(injectionRequest));
        verify(dummyLoggingService, times(1)).log(any());

        System.out.println(">>> TEST PASSED: " + response);
    }

    @Test
    void should_Pass_With_At_Transactional() {

        assertDoesNotThrow(() -> {
            var transactionalRequest = new TransactionalRequest("Hello Poshtar");
            String response = poshtar.send(transactionalRequest);
            assert response.equals("Request with Hello Poshtar") : "Response is incorrect";
            System.out.println(">>> TEST PASSED: " + response);

            EntityManager em = injector.getInstance(EntityManager.class);

            em.getTransaction().begin();
            List<TestEntity> results = em.createQuery("SELECT d FROM TestEntity d where d.data = 'Hello Poshtar'", TestEntity.class).getResultList();
            em.getTransaction().commit();
            assertFalse(results.isEmpty(), "Transaction did not commit!.");

            var entity = results.get(0);

            var updateRequest = new UpdateTransactionalRequest(entity.getId(), "Updated");
            poshtar.send(updateRequest);

            em.getTransaction().begin();
            List<TestEntity> updateResults = em.createQuery("SELECT d FROM TestEntity d where d.data = 'Updated'", TestEntity.class).getResultList();
            em.getTransaction().commit();
            assertFalse(updateResults.isEmpty(), "Transaction did not commit!.");

            verify(transactionalRequestHandler, times(1)).handle(eq(transactionalRequest));
            verify(updateTransactionalRequestHandler, times(1)).handle(eq(updateRequest));
        });

    }

    @Test
    void should_Fail_With_At_Transactional() {
        var request = new FailForTransactionalRequest("Fail for poshtar");
        Exception ex = assertThrowsExactly(RuntimeException.class, () -> {
            String response = poshtar.send(request);
            assert !response.equals("Fail for poshtar") : "Response is incorrect";
            System.out.println(">>> TEST PASSED: " + response);
        });
        String actual = ex.getMessage();
        String expected = "Simulated entity persistance failed!";
        assertEquals(expected, actual);

        EntityManager em = injector.getInstance(EntityManager.class);

        em.getTransaction().begin();
        List<TestEntity> results = em.createQuery("SELECT d FROM TestEntity d where d.data = 'Fail for poshtar'", TestEntity.class).getResultList();
        em.getTransaction().commit();
        assertTrue(results.isEmpty(), "Transaction did not roll back! Entity was saved.");

        verify(failForTransactionalRequestHandler, times(1)).handle(eq(request));
    }

}
