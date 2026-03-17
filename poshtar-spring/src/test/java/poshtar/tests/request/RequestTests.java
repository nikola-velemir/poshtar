package poshtar.tests.request;

import org.junit.jupiter.api.Test;
import org.nikola.velemir.poshtar.core.exceptions.HandlerNotFoundException;
import org.nikola.velemir.poshtar.core.mediator.Poshtar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.IllegalTransactionStateException;
import poshtar.tests.MockTransactionConfig;
import poshtar.tests.TestApplication;
import poshtar.tests.request.deps.infrastructure.NotFoundRequest;
import poshtar.tests.request.deps.injection.InjectionRequest;
import poshtar.tests.request.deps.injection.InjectionRequestHandler;
import poshtar.tests.request.deps.nullRequest.NullRequest;
import poshtar.tests.request.deps.ping.PingRequest;
import poshtar.tests.request.deps.ping.PingRequestHandler;
import poshtar.tests.request.deps.transactional.MandatoryRequest;
import poshtar.tests.request.deps.transactional.TransactionalRequest;
import poshtar.tests.request.deps.transactional.TransactionalRequestHandler;

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
        NullRequest request = null;
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> {
            poshtar.send(request);
        });
        assertInstanceOf(IllegalArgumentException.class, ex);
        String expected = "Request cannot be null";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    void should_fail_for_unregistered_handler() {
        NotFoundRequest request = new NotFoundRequest();
        Exception ex = assertThrowsExactly(HandlerNotFoundException.class, () -> {
            poshtar.send(request);
        });
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
        Exception ex = assertThrowsExactly(IllegalTransactionStateException.class, () -> {
            poshtar.send(request);

        });
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

        assert response.equals("Pong: Hello Poshtar") : "Odgovor nije ispravan!";
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
