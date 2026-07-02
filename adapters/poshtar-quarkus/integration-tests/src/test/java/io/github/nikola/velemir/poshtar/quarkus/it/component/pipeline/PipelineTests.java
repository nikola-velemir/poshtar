package io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline;

import io.github.nikola.velemir.poshtar.quarkus.it.component.TestRepository;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.dead.DeadRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.global.GlobalPipelineTestRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.mock.basic.BasicMockPipeline;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.mock.basic.BasicMockRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.mock.basic.BasicMockRequestHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.mock.hierarchy.HierarchyFirstBehaviour;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.mock.hierarchy.HierarchyRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.mock.hierarchy.HierarchyRequestHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.mock.hierarchy.HierarchySecondBehaviour;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.order.OrderRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.specific.NotSpecificRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.specific.SpecificRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.transactional.basic.fail.FailTransactionalRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.transactional.basic.success.TransactionalRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.transactional.mandatory.fail.FailMandatoryRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.transactional.mandatory.success.SucceedForMandatoryRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.validate.ValidationBehaviour;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.validate.ValidationRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.validate.ValidationRequestHandler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import jakarta.transaction.TransactionalException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@QuarkusTest
@OverruleNoInjection
public class PipelineTests {

    @InjectSpy
    Poshtar poshtar;
    @Inject
    TestRepository testRepository;

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
//        System.out.println("Bean Class Name: " + bean.getClass().getName());
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
        Exception ex = assertThrowsExactly(TransactionalException.class, () -> {
            poshtar.send(failMandatoryRequest);

        });
        String expectedMessage = "ARJUNA016110: Transaction is required for invocation";
        String actualMessage = ex.getMessage();
        assertEquals(expectedMessage, actualMessage);
        assertEquals(0, failMandatoryRequest.payload);
    }

    @Test
    void should_Pass_For_Mandatory() {

//        boolean beanExists = context.containsBean(SucceedForMandatoryPipeline.class.getName());
//        assert beanExists : "Pipeline bean has not been registered thru @PipelineBehaviour!";
//        Object bean = context.getBean(SucceedForMandatoryPipeline.class);
//        System.out.println("Bean Class Name: " + bean.getClass().getName());
//        Object handler = context.getBean(SucceedForMandatoryRequestHandler.class);
//        System.out.println("Bean Class Name: " + handler.getClass().getName());
        var succeedForMandatoryRequest = new SucceedForMandatoryRequest();
        assertDoesNotThrow(() -> {
            poshtar.send(succeedForMandatoryRequest);
        });
        assertEquals(1, succeedForMandatoryRequest.payload);
    }

    @Test
    void should_Work_For_Validation() {
//        assertTrue(
//                Arc.container().instance(ValidationBehaviour.class).isAvailable(),
//                "Handler bean not registered through @Handler!"
//        );
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

    @InjectSpy
    ValidationBehaviour validationBehaviour;
    @InjectSpy
    ValidationRequestHandler validationRequestHandler;
    @InjectSpy
    BasicMockRequestHandler basicMockrequestHandler;
    @InjectMock
    BasicMockPipeline basicMockPipeline;
    @InjectSpy
    HierarchyFirstBehaviour hierarchyFirstBehaviour;
    @InjectMock
    HierarchySecondBehaviour hierarchySecondBehaviour;
    @InjectSpy
    HierarchyRequestHandler hierarchyRequestHandler;


}
