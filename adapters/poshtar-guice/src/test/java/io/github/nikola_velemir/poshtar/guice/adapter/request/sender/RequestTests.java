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

package io.github.nikola_velemir.poshtar.guice.adapter.request.sender;

import com.google.inject.Injector;
import io.github.nikola_velemir.poshtar.core.exceptions.HandlerNotFoundException;
import io.github.nikola_velemir.poshtar.core.mediator.Sender;
import io.github.nikola_velemir.poshtar.guice.adapter.model.TestEntity;
import io.github.nikola_velemir.poshtar.guice.adapter.request.RequestTestsUtils;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining.ChainingFirstRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining.ChainingFirstRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining.ChainingSecondRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining.ChainingSecondRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.infrastructure.NotFoundRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.DummyLoggingService;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.InjectionRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.InjectionRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.InjectionResponse;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.mock.MockRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.mock.MockRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.mock.MockResponse;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.nullRequest.NullRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.ping.PingRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.ping.PingRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.fail.FailForTransactionalRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.fail.FailForTransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.TransactionalRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.TransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.UpdateTransactionalRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.UpdateTransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Exercises the {@link Sender} interface specifically (as opposed to
 * {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar} in the
 * sibling {@code mediator} test class). Both resolve to the same
 * {@code GuicePoshtar} singleton (see {@code PoshtarGuiceModule}).
 * <p>
 * NOTE: this class was previously typed and resolved as {@code Poshtar},
 * identical to the mediator test class, which made the split pointless.
 * It's been corrected here to use {@code Sender}, matching the pattern
 * used by the pipeline module's sender/mediator split.
 */
@OverruleNoInjection
public class RequestTests {
    public static Sender sender;
    private Injector injector;
    public static NullRequestHandler nullRequestHandler;
    public static PingRequestHandler pingRequestHandler;
    public static InjectionRequestHandler injectionRequestHandler;
    public static TransactionalRequestHandler transactionalRequestHandler;
    public static UpdateTransactionalRequestHandler updateTransactionalRequestHandler;
    public static FailForTransactionalRequestHandler failForTransactionalRequestHandler;
    public static DummyLoggingService dummyLoggingService;
    public static ChainingFirstRequestHandler chainingFirstRequestHandler;
    public static ChainingSecondRequestHandler chainingSecondRequestHandler;
    public static MockRequestHandler mockRequestHandler;

    static {
        java.util.logging.Logger.getLogger("com.google.inject.internal.ProxyFactory")
                .setLevel(java.util.logging.Level.SEVERE);
    }

    @BeforeEach
    void initTestContainer() {
        RequestTestsUtils.Mocks mocks = RequestTestsUtils.createMocks();
        mockRequestHandler = mocks.mockRequestHandler;

        dummyLoggingService = RequestTestsUtils.createLoggingSpy();

        Injector handlerInjector = RequestTestsUtils.buildHandlerInjector(dummyLoggingService);

        RequestTestsUtils.Spies spies = RequestTestsUtils.createSpies(handlerInjector, dummyLoggingService);
        nullRequestHandler = spies.nullRequestHandler;
        pingRequestHandler = spies.pingRequestHandler;
        injectionRequestHandler = spies.injectionRequestHandler;
        transactionalRequestHandler = spies.transactionalRequestHandler;
        updateTransactionalRequestHandler = spies.updateTransactionalRequestHandler;
        failForTransactionalRequestHandler = spies.failForTransactionalRequestHandler;
        chainingFirstRequestHandler = spies.chainingFirstRequestHandler;
        chainingSecondRequestHandler = spies.chainingSecondRequestHandler;

        injector = RequestTestsUtils.buildTestInjector(mocks, spies);

        sender = injector.getInstance(Sender.class);
    }

    @AfterEach
    void tearDown() {
        if (injector == null) return;

        EntityManager em = injector.getInstance(EntityManager.class);
        var tx = em.getTransaction();
        try {
            if (!tx.isActive()) {
                tx.begin();
            }
            em.createQuery("DELETE FROM TestEntity").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Test
    @Disabled
    void should_Chain_Accordingly() {
        var request = new ChainingFirstRequest();
        assertDoesNotThrow(() -> {
            var response = sender.send(request);
            assertEquals("Hello from second", response.getResponse());
        });
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

        MockResponse response = sender.send(firstMockRequest);

        assertNotNull(response);
        assertEquals("Hello", response.response());

        response = sender.send(secondMockRequest);

        assertNotNull(response);
        assertEquals("Author", response.response());

        verify(mockRequestHandler, times(1)).handle(eq(firstMockRequest));
        verify(mockRequestHandler, times(1)).handle(eq(secondMockRequest));
        verify(mockRequestHandler, times(2)).handle(any());
    }

    @Test
    void should_Register_And_Execute_Handler_Automatically() {
        PingRequest pingRequest = new PingRequest("Hello Poshtar");
        String response = sender.send(pingRequest);

        assert response.equals("Pong: Hello Poshtar") : "Wrong response!";
        verify(pingRequestHandler, times(1)).handle(eq(pingRequest));
    }

    @Test
    void handles_Null_Send() {
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> sender.send(null));
        assertInstanceOf(IllegalArgumentException.class, ex);
        assertEquals("Request cannot be null", ex.getMessage());
        verify(nullRequestHandler, never()).handle(any());
    }

    @Test
    void should_fail_for_unregistered_handler() {
        NotFoundRequest request = new NotFoundRequest();
        Exception ex = assertThrowsExactly(HandlerNotFoundException.class, () -> sender.send(request));
        assertInstanceOf(HandlerNotFoundException.class, ex);
        assertEquals("[PoshtaR] No handler found for type: [NotFoundRequest].", ex.getMessage());
    }

    @Test
    void should_Register_And_Inject_Service() {
        var injectionRequest = new InjectionRequest("Hello Poshtar");
        InjectionResponse response = sender.send(injectionRequest);

        assert response.payload().equals("Request with Logged: Hello Poshtar") : "Incorrect response!";
        verify(injectionRequestHandler, times(1)).handle(eq(injectionRequest));
        verify(dummyLoggingService, times(1)).log(any());
    }

    @Test
    void should_Pass_With_At_Transactional() {
        assertDoesNotThrow(() -> {
            var transactionalRequest = new TransactionalRequest("Hello Poshtar");
            String response = sender.send(transactionalRequest);
            assert response.equals("Request with Hello Poshtar") : "Response is incorrect";

            EntityManager em = injector.getInstance(EntityManager.class);

            em.getTransaction().begin();
            List<TestEntity> results = em.createQuery("SELECT d FROM TestEntity d where d.data = 'Hello Poshtar'", TestEntity.class).getResultList();
            em.getTransaction().commit();
            assertFalse(results.isEmpty(), "Transaction did not commit!.");

            var entity = results.get(0);

            var updateRequest = new UpdateTransactionalRequest(entity.getId(), "Updated");
            sender.send(updateRequest);

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
            String response = sender.send(request);
            assert !response.equals("Fail for poshtar") : "Response is incorrect";
        });
        assertEquals("Simulated entity persistance failed!", ex.getMessage());

        EntityManager em = injector.getInstance(EntityManager.class);

        em.getTransaction().begin();
        List<TestEntity> results = em.createQuery("SELECT d FROM TestEntity d where d.data = 'Fail for poshtar'", TestEntity.class).getResultList();
        em.getTransaction().commit();
        assertTrue(results.isEmpty(), "Transaction did not roll back! Entity was saved.");

        verify(failForTransactionalRequestHandler, times(1)).handle(eq(request));
    }
}