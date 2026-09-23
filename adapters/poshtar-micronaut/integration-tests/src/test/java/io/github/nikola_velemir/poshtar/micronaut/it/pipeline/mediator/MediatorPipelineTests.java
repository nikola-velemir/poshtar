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

package io.github.nikola_velemir.poshtar.micronaut.it.pipeline.mediator;


import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import io.github.nikola_velemir.poshtar.micronaut.adapter.runtime.internal.mediator.MicronautPoshtar;
import io.github.nikola_velemir.poshtar.micronaut.it.TestRepository;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.dead.DeadRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.global.GlobalPipelineTestRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.mock.basic.BasicMockPipeline;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.mock.basic.BasicMockRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.mock.basic.BasicMockRequestHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.mock.hierarchy.HierarchyFirstBehaviour;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.mock.hierarchy.HierarchyRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.mock.hierarchy.HierarchyRequestHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.mock.hierarchy.HierarchySecondBehaviour;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.order.OrderRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.specific.NotSpecificRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.specific.SpecificRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.transactional.basic.fail.FailTransactionalRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.transactional.basic.success.TransactionalRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.transactional.mandatory.fail.FailMandatoryRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.transactional.mandatory.success.SucceedForMandatoryRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.validate.ValidationBehaviour;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.validate.ValidationRequest;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.validate.ValidationRequestHandler;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import io.micronaut.context.annotation.Property;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.exceptions.IllegalTransactionStateException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
public class MediatorPipelineTests {

    @Inject
    Poshtar poshtar;
    @Inject
    TestRepository testRepository;
    @Inject
    ValidationBehaviour validationBehaviour;
    @Inject
    ValidationRequestHandler validationRequestHandler;
    @Inject
    BasicMockRequestHandler basicMockrequestHandler;
    @Inject
    BasicMockPipeline basicMockPipeline;
    @Inject
    HierarchyFirstBehaviour hierarchyFirstBehaviour;
    @Inject
    HierarchySecondBehaviour hierarchySecondBehaviour;
    @Inject
    HierarchyRequestHandler hierarchyRequestHandler;

    @Test
    void should_Call_Global_Pipeline() {
        assertDoesNotThrow(() -> {
            poshtar.send(new GlobalPipelineTestRequest());

        });
    }

    @Test
    void should_Respect_Order() {
        var orderRequest = new OrderRequest();
        assertDoesNotThrow(() -> {
            poshtar.send(orderRequest);

        });
        assertEquals(3, orderRequest.payload);
    }

    @Test
    void should_Call_Specific_Pipeline() {
        var specificRequest = new SpecificRequest();
        poshtar.send(specificRequest);
        assertEquals(1, specificRequest.payload);

        var notSpecificRequest = new NotSpecificRequest();
        poshtar.send(notSpecificRequest);
        assertEquals(0, notSpecificRequest.payload);
    }

    @Test
    void should_Fail_For_Transactional() {
//        boolean beanExists = context.containsBean(FailTransactionalPipeline.class.getName());
//        assert beanExists : "Pipeline bean has not been registered thru @Behaviour!";
//        Object bean = context.getBean(FailTransactionalPipeline.class);
//        System.out.println("Bean Class Name: " + bean.wn: io.micronaut.context.exceptions.ConfigurationException: No backing TransactionOperations configured. Check your configuration and try againgetClass().getName());
        var transactionalRequest = new FailTransactionalRequest("Fail transactional");
        Exception ex = assertThrowsExactly(RuntimeException.class, () -> {
            poshtar.send(transactionalRequest);

        });
        String expected = "Failing on purpose";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
        System.out.println(testRepository.findAll());
        var result = testRepository.findByData("Fail transactional");
        assertFalse(result.isPresent());
    }

    @Test
    void should_call_Dead_Pipeline() {
        var deadRequest = new DeadRequest();
        assertDoesNotThrow(() -> {
            var result = poshtar.send(deadRequest);
            assertNull(result);
        });
    }

    @Test
    void should_Pass_For_Transactional() {
//        boolean beanExists = context.containsBean(TransactionalPipeline.class.getName());
//        assert beanExists : "Pipeline bean has not been registered thru @PipelineBehaviour!";
//        Object bean = context.getBean(TransactionalPipeline.class);
//        System.out.println("Bean Class Name: " + bean.getClass().getName());
        var transactionalRequest = new TransactionalRequest();
        assertDoesNotThrow(() -> {
            poshtar.send(transactionalRequest);

        });
        System.out.println(testRepository.findAll());
        var result = testRepository.findByData("From transactional behaviour");
        assertTrue(result.isPresent());
        assertEquals(2, transactionalRequest.payload);
    }

    @Test
    void should_Fail_For_Mandatory() {
//        boolean beanExists = context.containsBean(FailMandatoryPipeline.class.getName());
//        assert beanExists : "Pipeline bean has not been registered thru @PipelineBehaviour!";
//        Object bean = context.getBean(FailMandatoryPipeline.class);
//        System.out.println("Bean Class Name: " + bean.getClass().getName());

        var failMandatoryRequest = new FailMandatoryRequest();
        Exception ex = assertThrowsExactly(IllegalTransactionStateException.class, () -> {
            poshtar.send(failMandatoryRequest);

        });
        String expectedMessage = "No existing transaction found for transaction marked with propagation 'mandatory'";
        String actualMessage = ex.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(0, failMandatoryRequest.payload);
    }

    @Test
    void should_Pass_For_Mandatory() {

        var succeedForMandatoryRequest = new SucceedForMandatoryRequest();
        assertDoesNotThrow(() -> {
            poshtar.send(succeedForMandatoryRequest);
        });
        assertEquals(1, succeedForMandatoryRequest.payload);
    }

    @Test
    void should_Work_For_Validation() {

        var goodValidationRequest = new ValidationRequest(1);
        assertDoesNotThrow(() -> {
            var response = poshtar.send(goodValidationRequest);
            assertEquals(2, response);
        });
        var badValidationRequest = new ValidationRequest(0);
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> {
            poshtar.send(badValidationRequest);
        });
        assertEquals(0, badValidationRequest.payload());
        String actual = ex.getMessage();
        String expected = "Payload is wrong";
        assertEquals(expected, actual);


        verify(validationRequestHandler, times(1)).handle(eq(goodValidationRequest));
        verify(validationBehaviour, times(1)).handle(eq(goodValidationRequest), any(RequestDelegate.class));

        verify(validationRequestHandler, never()).handle(eq(badValidationRequest));
        verify(validationBehaviour, times(1)).handle(eq(badValidationRequest), any(RequestDelegate.class));

        verify(poshtar, times(1)).send(eq(goodValidationRequest));
        verify(poshtar, times(1)).send(eq(badValidationRequest));

        verify(poshtar, times(2)).send(any());
    }

    @Test
    void should_Mock_Basic() {
        var request = new BasicMockRequest();
        when(basicMockPipeline.handle(eq(request), any(RequestDelegate.class))).thenReturn("Did not pass");
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
    void should_Mock_Hierarchy() {
        var request = new HierarchyRequest();
        when(hierarchySecondBehaviour.handle(eq(request), any(RequestDelegate.class))).thenReturn("I miss the handler :(");
        assertDoesNotThrow(() -> {
            var response = poshtar.send(request);
            assertEquals("I miss the handler :(", response);
        });
        verify(hierarchyFirstBehaviour, times(1)).handle(eq(request), any(RequestDelegate.class));
        verify(hierarchySecondBehaviour, times(1)).handle(eq(request), any(RequestDelegate.class));

        verify(hierarchyRequestHandler, never()).handle(eq(request));
        verify(hierarchyRequestHandler, never()).handle(any());
        verify(poshtar, times(1)).send(eq(request));
    }

    //
    // =========================================================================
    // Micronaut Mock/Spy Factory Definitions
    // =========================================================================
    //
    // - Plain spies (ValidationBehaviour, ValidationRequestHandler, BasicMockRequestHandler,
    //   HierarchyFirstBehaviour, HierarchyRequestHandler) use MANUAL CONSTRUCTION, never an
    //   injected `target` parameter — the delegate is a bare POJO BeanContext never resolves,
    //   so there's no generated dispatch class for the Mockito mock to recurse against.
    //
    // - BasicMockPipeline / HierarchySecondBehaviour are pure test doubles (Quarkus @InjectMock
    //   equivalents) — plain mock(), no delegation, no real logic behind them.
    //
    // - Poshtar IS mocked here, but via manual construction of the real MicronautPoshtar
    //   implementation from its two (unmocked) registry dependencies, exactly as in
    //   PublisherNotificationTests — NOT via an injected `target: Poshtar` parameter. Poshtar
    //   is produced by a multi-argument @Factory method; accepting it as an injected parameter
    //   here would re-trigger the self-referential @Replaces resolution that caused
    //   StackOverflowError earlier in this project. RequestRegistry/NotificationRegistry are
    //   NOT themselves mocked, so resolving them normally as parameters is safe.

    @MockBean(Poshtar.class)
    Poshtar poshtarSpy(RequestRegistry requestRegistry, NotificationRegistry notificationRegistry,  @Named(TaskExecutors.IO) ExecutorService executorService ) {
        return mock(Poshtar.class, delegatesTo(new MicronautPoshtar(requestRegistry, notificationRegistry, executorService)));
    }

    @MockBean(ValidationBehaviour.class)
    ValidationBehaviour validationBehaviourSpy() {
        return mock(ValidationBehaviour.class, delegatesTo(new ValidationBehaviour()));
    }

    @MockBean(ValidationRequestHandler.class)
    ValidationRequestHandler validationRequestHandlerSpy() {
        return mock(ValidationRequestHandler.class, delegatesTo(new ValidationRequestHandler()));
    }

    @MockBean(BasicMockRequestHandler.class)
    BasicMockRequestHandler basicMockrequestHandlerSpy() {
        return mock(BasicMockRequestHandler.class, delegatesTo(new BasicMockRequestHandler()));
    }

    @MockBean(BasicMockPipeline.class)
    BasicMockPipeline basicMockPipelineMock() {
        return mock(BasicMockPipeline.class); // No delegation — pure test double.
    }

    @MockBean(HierarchyFirstBehaviour.class)
    HierarchyFirstBehaviour hierarchyFirstBehaviourSpy() {
        return mock(HierarchyFirstBehaviour.class, delegatesTo(new HierarchyFirstBehaviour()));
    }

    @MockBean(HierarchySecondBehaviour.class)
    HierarchySecondBehaviour hierarchySecondBehaviourMock() {
        return mock(HierarchySecondBehaviour.class); // No delegation — pure test double.
    }

    @MockBean(HierarchyRequestHandler.class)
    HierarchyRequestHandler hierarchyRequestHandlerSpy() {
        return mock(HierarchyRequestHandler.class, delegatesTo(new HierarchyRequestHandler()));
    }
}