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

package io.github.nikola_velemir.poshtar.spring.adapter.request;

import io.github.nikola_velemir.poshtar.core.exceptions.HandlerNotFoundException;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
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

@SpringBootTest(classes = TestApplication.class)
@Import(MockTransactionConfig.class)
public class RequestTests {
    @Autowired
    private Poshtar poshtar;
    @Autowired
    private ApplicationContext context;
    @Test
    void handles_Null_Send() {
        System.out.println(RequestHandler.class.getSimpleName());
        NullRequest request = null;
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> poshtar.send(request));
        assertInstanceOf(IllegalArgumentException.class, ex);
        String expected = "Request cannot be null";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
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
    void should_Pass_With_At_Transactional() {
        boolean beanExists = context.containsBean(TransactionalRequestHandler.class.getName());
        assert beanExists : "Handler bean has not been registered thru @RequestHandler!";
        assertDoesNotThrow(() -> {
            String response = poshtar.send(new TransactionalRequest("Hello Poshtar"));
            assert response.equals("Request with Hello Poshtar") : "Response is incorrect";
            System.out.println(">>> TEST PASSED: " + response);
        });


    }

    @Test
    void should_Fail_For_Mandatory_Propagation() {
        var request = new MandatoryRequest("Payload");
        Exception ex = assertThrowsExactly(IllegalTransactionStateException.class, () -> poshtar.send(request));
        assertInstanceOf(IllegalTransactionStateException.class, ex);
        String expectedMessage = "No existing transaction found for transaction marked with propagation 'mandatory'";
        String actualMessage = ex.getMessage();
        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    void should_Register_And_Execute_Handler_Automatically() {

        boolean beanExists = context.containsBean(PingRequestHandler.class.getName());
        assert beanExists : "Handler bean not registered thru @RequestHandler!";

        String response = poshtar.send(new PingRequest("Hello Poshtar"));

        assert response.equals("Pong: Hello Poshtar") : "Wrong response!";
        System.out.println(">>> TEST PASSED: " + response);
    }

    @Test
    void should_Register_And_Inject_Service() {

        boolean beanExists = context.containsBean(InjectionRequestHandler.class.getName());
        assert beanExists : "Handler not registered thru @RequestHandler!";

        String response = poshtar.send(new InjectionRequest("Hello Poshtar"));

        assert response.equals("Request with Logged: Hello Poshtar") : "Incorrect response!";
        System.out.println(">>> TEST PROŠAO: " + response);
    }
}
