package poshtar.tests.request.ping;

import adapter.EnablePoshtar;
import org.example.core.mediator.IMediator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import poshtar.tests.TestApplication;
@SpringBootTest(classes = {TestApplication.class})
public class PingIntegrationTest {
    @Autowired
    private IMediator mediator;
    @Autowired
    private ApplicationContext context;
    @Test
    void should_Register_And_Execute_Handler_Automatically() throws Exception {

        boolean beanExists = context.containsBean(PingRequestHandler.class.getName());
        assert beanExists : "Handler bean nije registrovan preko @RequestHandler!";

        String response = mediator.send(new PingRequest("Hello Poshtar"));

        assert response.equals("Pong: Hello Poshtar") : "Odgovor nije ispravan!";
        System.out.println(">>> TEST PROŠAO: " + response);
    }
}

