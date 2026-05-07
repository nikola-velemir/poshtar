/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.nikola_velemir.poshtar.spring.adapter.pipeline;

import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import org.junit.jupiter.api.Test;
import io.github.nikola_velemir.poshtar.spring.adapter.MockTransactionConfig;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.fail.FailTransactionalPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.fail.FailTransactionalRequest;
import io.github.nikola_velemir.poshtar.spring.adapter.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
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

@SpringBootTest(classes = TestApplication.class)
@Import(MockTransactionConfig.class)
public class PipelineTests {
    @Autowired
    private Poshtar poshtar;
    @Autowired
    private ApplicationContext context;

    @Autowired
    private TestRepository repository;

    @Test
    void should_Call_Global_Pipeline() {
        assertDoesNotThrow(() -> {
            poshtar.send(new GlobalPipelineTestRequest());

        });
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
    void should_call_Dead_Pipeline() {
        var deadRequest = new DeadRequest();
        assertDoesNotThrow(() -> {
            var result = poshtar.send(deadRequest);
            assertNull(result);
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
        var badValidatioNRequest = new ValidationRequest(0);
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> {
            poshtar.send(badValidatioNRequest);
        });
        assertEquals(0, badValidatioNRequest.payload());
        String actual = ex.getMessage();
        String expected = "Payload is wrong";
        assertEquals(expected, actual);
    }
}
