package poshtar.tests.request;

import org.example.core.exceptions.HandlerNotFoundException;
import org.example.core.mediator.IMediator;
import org.junit.jupiter.api.Test;
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
    private IMediator mediator;
    @Autowired
    private ApplicationContext context;

    @Test
    void handles_Null_Send() {
        NullRequest request = null;
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> {
            mediator.send(request);
        });
        String expected = "Request cannot be null";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    void should_fail_for_unregistered_handler() {
        NotFoundRequest request = new NotFoundRequest();
        Exception exception = assertThrowsExactly(HandlerNotFoundException.class, () -> {
            mediator.send(request);
        });
        String expectedMessage = "[PoshtaR] No handler found for type: [NotFoundRequest].";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void should_Pass_With_At_Transactional() {
        boolean beanExists = context.containsBean(TransactionalRequestHandler.class.getName());
        assert beanExists : "Handler bean has not been registered thru @RequestHandler!";
        assertDoesNotThrow(() -> {
            String response = mediator.send(new TransactionalRequest("Hello Poshtar"));
            assert response.equals("Request with Hello Poshtar") : "Response is incorrect";
            System.out.println(">>> TEST PASSED: " + response);
        });


    }

    @Test
    void should_Fail_For_Mandatory_Propagation() {
        var request = new MandatoryRequest("Payload");
        Exception ex = assertThrowsExactly(IllegalTransactionStateException.class, () -> {
            mediator.send(request);

        });
        String expectedMessage = "No existing transaction found for transaction marked with propagation 'mandatory'";
        String actualMessage = ex.getMessage();
        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    void should_Register_And_Execute_Handler_Automatically() {

        boolean beanExists = context.containsBean(PingRequestHandler.class.getName());
        assert beanExists : "Handler bean nije registrovan preko @RequestHandler!";

        String response = mediator.send(new PingRequest("Hello Poshtar"));

        assert response.equals("Pong: Hello Poshtar") : "Odgovor nije ispravan!";
        System.out.println(">>> TEST PROŠAO: " + response);
    }

    @Test
    void debug_handler_proxy() {
        Object handler = context.getBean(TransactionalRequestHandler.class);
        System.out.println(">>> KLASA HANDLERA: " + handler.getClass().getName());
    }

    @Test
    void should_Register_And_Inject_Service() {

        boolean beanExists = context.containsBean(InjectionRequestHandler.class.getName());
        assert beanExists : "Handler bean nije registrovan preko @RequestHandler!";

        String response = mediator.send(new InjectionRequest("Hello Poshtar"));

        assert response.equals("Request with Logged: Hello Poshtar") : "Odgovor nije ispravan!";
        System.out.println(">>> TEST PROŠAO: " + response);
    }
}
