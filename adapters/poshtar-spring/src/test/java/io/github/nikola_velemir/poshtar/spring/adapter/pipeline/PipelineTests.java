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

package io.github.nikola_velemir.poshtar.spring.adapter.pipeline;

import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.dead.DeadPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.dead.DeadPipelineCatcher;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.dead.DeadRequestHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.global.GlobalTestPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.order.OrderFirstPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.order.OrderRequestHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.order.OrderSecondPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.specific.SpecificPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.fail.FailTransactionalHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.success.TransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.fail.FailMandatoryRequestHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.validate.ValidationRequestHandler;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import org.junit.jupiter.api.Test;
import io.github.nikola_velemir.poshtar.spring.adapter.MockTransactionConfig;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.fail.FailTransactionalPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.fail.FailTransactionalRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.repository.TestRepository;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.IllegalTransactionStateException;
import io.github.nikola_velemir.poshtar.spring.adapter.TestApplication;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.dead.DeadRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.global.GlobalPipelineTestRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.order.OrderRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.specific.NotSpecificRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.specific.SpecificRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.success.TransactionalPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.success.TransactionalRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.fail.FailMandatoryPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.fail.FailMandatoryRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.success.SucceedForMandatoryPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.success.SucceedForMandatoryRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.success.SucceedForMandatoryRequestHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.validate.ValidationBehaviour;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.validate.ValidationRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@OverruleNoInjection
@SuppressWarnings("unchecked")
@SpringBootTest(classes = TestApplication.class)
@Import(MockTransactionConfig.class)
public class PipelineTests {
    @Autowired
    @MockitoSpyBean
    private Poshtar poshtar;
    @Autowired
    private ApplicationContext context;

    @Autowired
    private TestRepository repository;


    @Test
    void should_Call_Global_Pipeline_Exactly_Once() {
        var request = new GlobalPipelineTestRequest();

        poshtar.send(request);

        verify(testPipeline, times(1)).handle(eq(request), any(RequestDelegate.class));
        verify(poshtar, times(1)).send(eq(request));
    }

    @Test
    void should_Call_Specific_Pipeline() {
        var specificRequest = new SpecificRequest();
        poshtar.send(specificRequest);
        assertEquals(1, specificRequest.payload);

        var notSpecificRequest = new NotSpecificRequest();
        poshtar.send(notSpecificRequest);
        assertEquals(0, notSpecificRequest.payload);

        verify(testPipeline, times(1)).handle(eq(specificRequest), any(RequestDelegate.class));
        verify(specificPipeline, times(1)).handle(eq(specificRequest), any(RequestDelegate.class));
        verify(testPipeline, times(1)).handle(eq(notSpecificRequest), any(RequestDelegate.class));
        verify(testPipeline, times(2)).handle(any(), any(RequestDelegate.class));



        verify(poshtar, times(1)).send(eq(specificRequest));
        verify(poshtar, times(1)).send(eq(notSpecificRequest));
        verify(poshtar, times(2)).send(any());


    }

    @Test
    void should_call_Dead_Pipeline() {
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

        verify(poshtar, times(1)).send(eq(deadRequest));
        verify(poshtar, times(1)).send(any());
    }

    @Test
    void should_Respect_Order() {
        var orderRequest = new OrderRequest();
        assertDoesNotThrow(() -> {
            poshtar.send(orderRequest);

        });
        assertEquals(3, orderRequest.payload);
        verify(orderFirstPipeline, times(1)).handle(eq(orderRequest), any(RequestDelegate.class));
        verify(orderSecondPipeline, times(1)).handle(eq(orderRequest), any(RequestDelegate.class));
        verify(orderRequestHandler, times(1)).handle(eq(orderRequest));


        verify(poshtar, times(1)).send(eq(orderRequest));
        verify(poshtar, times(1)).send(any());

    }

    @Test
    void should_Fail_For_Transactional() {
        boolean beanExists = context.containsBean(FailTransactionalPipeline.class.getName());
        assert beanExists : "Pipeline bean has not been registered thru @Behaviour!";
        Object bean = context.getBean(FailTransactionalPipeline.class);
        System.out.println("Bean Class Name: " + bean.getClass().getName());
        var transactionalRequest = new FailTransactionalRequest("Fail transactional");
        Exception ex = assertThrowsExactly(RuntimeException.class, () -> {
            poshtar.send(transactionalRequest);

        });
        String expected = "Failing on purpose";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
        System.out.println(repository.findAll());
        var result = repository.findByData("Fail transactional");
        assertNull(result);

        verify(failTransactionalPipeline, times(1)).handle(eq(transactionalRequest), any(RequestDelegate.class));
        verify(failTransactionalHandler, never()).handle(eq(transactionalRequest));


        verify(poshtar, times(1)).send(eq(transactionalRequest));
        verify(poshtar, times(1)).send(any());

    }

    @Test
    void should_Pass_For_Transactional() {
        boolean beanExists = context.containsBean(TransactionalPipeline.class.getName());
        assert beanExists : "Pipeline bean has not been registered thru @PipelineBehaviour!";
        Object bean = context.getBean(TransactionalPipeline.class);
        System.out.println("Bean Class Name: " + bean.getClass().getName());
        var transactionalRequest = new TransactionalRequest();
        assertDoesNotThrow(() -> {
            poshtar.send(transactionalRequest);

        });
        System.out.println(repository.findAll());
        var result = repository.findByData("From transactional behaviour");
        assertNotNull(result);
        assertEquals(2, transactionalRequest.payload);

        verify(transactionalPipeline, times(1)).handle(eq(transactionalRequest), any(RequestDelegate.class));
        verify(transactionalRequestHandler, times(1)).handle(eq(transactionalRequest));

        verify(poshtar, times(1)).send(eq(transactionalRequest));
        verify(poshtar, times(1)).send(any());

    }

    @Test
    void should_Fail_For_Mandatory() {
        boolean beanExists = context.containsBean(FailMandatoryPipeline.class.getName());
        assert beanExists : "Pipeline bean has not been registered thru @PipelineBehaviour!";
        Object bean = context.getBean(FailMandatoryPipeline.class);
        System.out.println("Bean Class Name: " + bean.getClass().getName());

        var failMandatoryRequest = new FailMandatoryRequest();
        Exception ex = assertThrowsExactly(IllegalTransactionStateException.class, () -> {
            poshtar.send(failMandatoryRequest);

        });
        String expectedMessage = "No existing transaction found for transaction marked with propagation 'mandatory'";
        String actualMessage = ex.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(0, failMandatoryRequest.payload);

        verify(failMandatoryPipeline, never()).handle(eq(failMandatoryRequest), any(RequestDelegate.class));
        verify(failMandatoryRequestHandler, never()).handle(eq(failMandatoryRequest));

        verify(poshtar, times(1)).send(eq(failMandatoryRequest));
        verify(poshtar, times(1)).send(any());

    }

    @Test
    void should_Pass_For_Mandatory() {

        boolean beanExists = context.containsBean(SucceedForMandatoryPipeline.class.getName());
        assert beanExists : "Pipeline bean has not been registered thru @PipelineBehaviour!";
        Object bean = context.getBean(SucceedForMandatoryPipeline.class);
        System.out.println("Bean Class Name: " + bean.getClass().getName());
        Object handler = context.getBean(SucceedForMandatoryRequestHandler.class);
        System.out.println("Bean Class Name: " + handler.getClass().getName());
        var succeedForMandatoryRequest = new SucceedForMandatoryRequest();
        assertDoesNotThrow(() -> {
            poshtar.send(succeedForMandatoryRequest);
        });
        assertEquals(1, succeedForMandatoryRequest.payload);
        verify(succeedForMandatoryPipeline, times(1)).handle(eq(succeedForMandatoryRequest), any(RequestDelegate.class));
        verify(succeedForMandatoryRequestHandler, times(1)).handle(eq(succeedForMandatoryRequest));


        verify(poshtar, times(1)).send(eq(succeedForMandatoryRequest));
        verify(poshtar, times(1)).send(any());

    }

    @Test
    void should_Work_For_Validation() {
        boolean beanExists = context.containsBean(ValidationBehaviour.class.getName());
        assert beanExists : "Pipeline bean has not been registered thru @PipelineBehaviour!";

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

        verify(validationRequestHandler,never()).handle(eq(badValidationRequest));
        verify(validationBehaviour, times(1)).handle(eq(badValidationRequest), any(RequestDelegate.class));

        verify(poshtar, times(1)).send(eq(goodValidationRequest));
        verify(poshtar, times(1)).send(eq(badValidationRequest));

        verify(poshtar, times(2)).send(any());
    }

    @MockitoSpyBean // 👈 This intercepts the bean inside the Spring Container
    private GlobalTestPipeline testPipeline;
    @MockitoSpyBean
    private SpecificPipeline specificPipeline;

    @MockitoSpyBean
    private DeadPipeline deadPipeline;
    @MockitoSpyBean
    private DeadPipelineCatcher deadPipelineCatcher;
    @MockitoSpyBean
    private DeadRequestHandler deadRequestHandler;
    @MockitoSpyBean
    private OrderFirstPipeline orderFirstPipeline;
    @MockitoSpyBean
    private OrderSecondPipeline orderSecondPipeline;
    @MockitoSpyBean
    private OrderRequestHandler orderRequestHandler;
    @MockitoSpyBean
    private FailTransactionalPipeline failTransactionalPipeline;
    @MockitoSpyBean
    private FailTransactionalHandler failTransactionalHandler;
    @MockitoSpyBean
    private TransactionalPipeline transactionalPipeline;
    @MockitoSpyBean
    private TransactionalRequestHandler transactionalRequestHandler;
    @MockitoSpyBean
    private FailMandatoryPipeline failMandatoryPipeline;
    @MockitoSpyBean
    private FailMandatoryRequestHandler failMandatoryRequestHandler;
    @MockitoSpyBean
    private SucceedForMandatoryPipeline succeedForMandatoryPipeline;
    @MockitoSpyBean
    private SucceedForMandatoryRequestHandler succeedForMandatoryRequestHandler;
    @MockitoSpyBean
    private ValidationBehaviour validationBehaviour;
    @MockitoSpyBean
    private ValidationRequestHandler validationRequestHandler;
}
