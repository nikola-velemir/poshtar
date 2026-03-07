package poshtar.tests.request.ping;

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
    void should_Register_And_Execute_Handler_Automatically() {
        // PROVERA 1: Da li je skener napravio bean od našeg handlera?
        // Iako nismo stavili @Component, Spring bi trebalo da ga vidi
        boolean beanExists = context.containsBean(PingRequestHandler.class.getName());
        assert beanExists : "Handler bean nije registrovan preko @RequestHandler!";

        // PROVERA 2: Da li Mediator može da pošalje poruku i dobije odgovor?
        String response = mediator.send(new PingRequest("Hello Poshtar"));

        assert response.equals("Pong: Hello Poshtar") : "Odgovor nije ispravan!";
        System.out.println(">>> TEST PROŠAO: " + response);
    }
}

