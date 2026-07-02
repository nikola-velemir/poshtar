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

package io.github.nikola_velemir.poshtar.guice.adapter.pipeline;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.dead.DeadPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.dead.DeadPipelineCatcher;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.dead.DeadRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.global.GlobalPipelineTestRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.global.GlobalTestPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.basic.BasicMockPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.basic.BasicMockRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.basic.BasicMockRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.hierarchy.HierarchyFirstBehaviour;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.hierarchy.HierarchyRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.hierarchy.HierarchyRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.hierarchy.HierarchySecondBehaviour;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.order.OrderFirstPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.order.OrderRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.order.OrderSecondPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.specific.SpecificPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.fail.FailTransactionalPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.fail.FailTransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.success.TransactionalPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.success.TransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.validate.ValidationBehaviour;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.validate.ValidationRequestHandler;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.guice.adapter.TestModule;
import io.github.nikola_velemir.poshtar.guice.adapter.model.TestEntity;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.dead.DeadRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.order.OrderRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.specific.NotSpecificRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.specific.SpecificRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.fail.FailTransactionalRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.success.TransactionalRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.validate.ValidationRequest;
import org.mockito.Mockito;


import java.util.List;

import static io.github.nikola_velemir.poshtar.guice.adapter.pipeline.PipelineTestsUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@SuppressWarnings("rawtypes")
@OverruleNoInjection
public class PipelineTests {
    static FailTransactionalPipeline failTransactionalPipeline;
    static FailTransactionalRequestHandler failTransactionalHandler;
    static TransactionalPipeline transactionalPipeline;
    static TransactionalRequestHandler transactionalHandler;
    private Poshtar poshtar;
    private Injector injector;
    static GlobalTestPipeline globalPipeline;
    static SpecificPipeline specificPipeline;
    static DeadPipeline deadPipeline;
    static DeadPipelineCatcher deadPipelineCatcher;
    static DeadRequestHandler deadRequestHandler;
    static OrderFirstPipeline orderFirstPipeline;
    static OrderSecondPipeline orderSecondPipeline;
    static OrderRequestHandler orderRequestHandler;
    static ValidationRequestHandler validationRequestHandler;
    static ValidationBehaviour validationBehaviour;
    static BasicMockRequestHandler basicMockrequestHandler;
    static BasicMockPipeline basicMockPipeline;
    static HierarchyFirstBehaviour hierarchyFirstBehaviour;
    static HierarchySecondBehaviour hierarchySecondBehaviour;
    static HierarchyRequestHandler hierarchyRequestHandler;

    @BeforeEach
    void initTestContainer() {
        createMocks();

        Injector behaviourInjector = Guice.createInjector(Modules.override(new TestModule()).with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(BasicMockPipeline.class).toInstance(PipelineTests.basicMockPipeline);

                bind(HierarchySecondBehaviour.class).toInstance(PipelineTests.hierarchySecondBehaviour);
            }
        }));
        createSpies(behaviourInjector);

        injector = buildTestInjector();
        poshtar = injector.getInstance(Poshtar.class);

        EntityManager em = injector.getInstance(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM TestEntity").executeUpdate();
        em.getTransaction().commit();
    }


    @AfterEach
    void tearDown() {
        EntityManager em = injector.getInstance(EntityManager.class);
        if (em.isOpen()) em.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    void should_Call_Global_Pipeline_Exactly_Once() {
        var request = new GlobalPipelineTestRequest();

        poshtar.send(request);

        verify(globalPipeline, times(1)).handle(eq(request), any(RequestDelegate.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void should_Call_Specific_Pipeline() {
        var specificRequest = new SpecificRequest();
        poshtar.send(specificRequest);
        assertEquals(1, specificRequest.payload);

        var notSpecificRequest = new NotSpecificRequest();
        poshtar.send(notSpecificRequest);
        assertEquals(0, notSpecificRequest.payload);
        verify(globalPipeline, times(1)).handle(eq(specificRequest), any(RequestDelegate.class));
        verify(specificPipeline, times(1)).handle(eq(specificRequest), any(RequestDelegate.class));
        verify(globalPipeline, times(1)).handle(eq(notSpecificRequest), any(RequestDelegate.class));
        verify(globalPipeline, times(2)).handle(any(), any(RequestDelegate.class));
    }

    @Test
    @Disabled
    void should_Mock_Basic() {
        var request = new BasicMockRequest();
        when(basicMockPipeline.handle(eq(request), any())).thenReturn("Did not pass");
        assertDoesNotThrow(() -> {
            var response = poshtar.send(request);
            assertEquals("Did not pass", response);
        });
        verify(basicMockPipeline, times(1)).handle(eq(request), any(RequestDelegate.class));
        verify(basicMockrequestHandler, times(0)).handle(eq(request));
        verify(basicMockrequestHandler, never()).handle(any());
        verify(poshtar, times(1)).send(eq(request));

    }

    @Test
    @Disabled
    void should_Mock_Hierarchy() {
        // 1. Grab the active registry from the system
        RequestRegistry registry = injector.getInstance(RequestRegistry.class);

        // Print out the exact object classes inside your registry to see if it's a Mockito mock or a real object
        System.out.println("REGISTRY TYPE: " + registry.getClass().getName());

        var request = new HierarchyRequest();

        // 2. STUBBING WITH THE PURE MOCK
        when(hierarchySecondBehaviour.handle(eq(request), any()))
                .thenReturn("I miss the handler :(");

        assertDoesNotThrow(() -> {
            var response = poshtar.send(request);
            assertEquals("I miss the handler :(", response);
        });
    }
    @Test
    void should_Call_Dead_Pipeline() {
        var deadRequest = new DeadRequest();
        assertDoesNotThrow(() -> {
            var result = poshtar.send(deadRequest);
            assertNull(result);
        });
        verify(deadPipeline, times(1)).handle(eq(deadRequest), any(RequestDelegate.class));
        verify(deadPipelineCatcher, never()).handle(eq(deadRequest), any(RequestDelegate.class));
        verify(deadPipelineCatcher, never()).handle(any(), any());
        verify(deadRequestHandler, never()).handle(eq(deadRequest));
        verify(deadRequestHandler, never()).handle(any());
    }

    @Test
    void should_Respect_Order() {
        var orderRequest = new OrderRequest();
        assertDoesNotThrow(() -> poshtar.send(orderRequest));
        assertEquals(3, orderRequest.payload);
        verify(orderFirstPipeline, times(1)).handle(eq(orderRequest), any(RequestDelegate.class));
        verify(orderSecondPipeline, times(1)).handle(eq(orderRequest), any(RequestDelegate.class));
        verify(orderRequestHandler, times(1)).handle(eq(orderRequest));
    }

    @Test
    void should_Work_For_Validation() {
        var goodValidationRequest = new ValidationRequest(1);
        assertDoesNotThrow(() -> {
            var response = poshtar.send(goodValidationRequest);
            assertEquals(2, response);
        });

        var badValidationRequest = new ValidationRequest(0);
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> poshtar.send(badValidationRequest));
        assertEquals(0, badValidationRequest.payload());
        assertEquals("Payload is wrong", ex.getMessage());

        verify(validationRequestHandler, times(1)).handle(eq(goodValidationRequest));
        verify(validationBehaviour, times(1)).handle(eq(goodValidationRequest), any(RequestDelegate.class));

        verify(validationRequestHandler, never()).handle(eq(badValidationRequest));
        verify(validationBehaviour, times(1)).handle(eq(badValidationRequest), any(RequestDelegate.class));

    }

    @Test
    void should_Pass_For_Transactional() {
        var transactionalRequest = new TransactionalRequest();
        assertDoesNotThrow(() -> poshtar.send(transactionalRequest));
        assertEquals(2, transactionalRequest.payload);

        EntityManager em = injector.getInstance(EntityManager.class);
        em.getTransaction().begin();
        List<TestEntity> results = em.createQuery("SELECT d FROM TestEntity d WHERE d.data = 'From transactional pipeline'", TestEntity.class).getResultList();
        em.getTransaction().commit();

        assertFalse(results.isEmpty(), "Transaction did not commit");
        assertEquals(1, results.size());

        verify(transactionalPipeline, times(1)).handle(eq(transactionalRequest), any(RequestDelegate.class));
        verify(transactionalHandler, times(1)).handle(eq(transactionalRequest));
    }

    @Test
    void should_Fail_For_Transactional() {
        var failTransactionalRequest = new FailTransactionalRequest("Fail from transactional pipeline");
        assertThrowsExactly(RuntimeException.class, () -> poshtar.send(failTransactionalRequest));

        EntityManager em = injector.getInstance(EntityManager.class);
        em.getTransaction().begin();
        List<TestEntity> results = em.createQuery("SELECT d FROM TestEntity d WHERE d.data = 'Fail from transactional pipeline'", TestEntity.class).getResultList();
        em.getTransaction().commit();

        assertTrue(results.isEmpty(), "Transaction should have rolled back");

        verify(failTransactionalPipeline, times(1)).handle(eq(failTransactionalRequest), any(RequestDelegate.class));
        verify(failTransactionalHandler, never()).handle(eq(failTransactionalRequest));
    }
}