package poshtar.tests.request;

import org.example.core.mediator.IMediator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import poshtar.tests.TestApplication;
import poshtar.tests.request.deps.ping.PingRequest;
import poshtar.tests.request.deps.ping.PingRequestHandler;
import poshtar.tests.request.deps.transactional.TransactionalRequest;
import poshtar.tests.request.deps.transactional.TransactionalRequestHandler;

@SpringBootTest(classes = TestApplication.class)
public class RequestTests {
    @Autowired
    private IMediator mediator;
    @Autowired
    private ApplicationContext context;
    @Test
    void should_Pass_With_At_Transactional() {
        boolean beanExists = context.containsBean(TransactionalRequestHandler.class.getName());
        assert beanExists : "Handler bean has not been registered thru @RequestHandler!";
        String response = mediator.send(new TransactionalRequest("Hello Poshtar"));

        assert response.equals("Request with Hello Poshtar") : "Response is incorrect";
        System.out.println(">>> TEST PASSED: " + response);
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
    void should_Register_And_Inject_Service() {

        boolean beanExists = context.containsBean(poshtar.tests.request.deps.injection.InjectionRequestHandler.class.getName());
        assert beanExists : "Handler bean nije registrovan preko @RequestHandler!";

        String response = mediator.send(new poshtar.tests.request.deps.injection.InjectionRequest("Hello Poshtar"));

        assert response.equals("Request with Logged: Hello Poshtar") : "Odgovor nije ispravan!";
        System.out.println(">>> TEST PROŠAO: " + response);
    }
}
