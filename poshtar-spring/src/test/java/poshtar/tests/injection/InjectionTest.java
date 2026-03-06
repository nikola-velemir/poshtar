package poshtar.tests.injection;

import org.example.core.mediator.IMediator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import poshtar.tests.TestApplication;

@SpringBootTest(classes = {TestApplication.class})
class InjectionTest {
    @Autowired
    private IMediator mediator; // Sada bi trebalo da pozeleni
    @Autowired
    private ApplicationContext context;
    @Test
    void should_Register_And_Execute_Handler_with_dependency() {
        // PROVERA 1: Da li je skener napravio bean od našeg handlera?
        // Iako nismo stavili @Component, Spring bi trebalo da ga vidi
        boolean beanExists = context.containsBean(InjectionRequestHandler.class.getName());
        assert beanExists : "Handler bean nije registrovan preko @RequestHandler!";

        // PROVERA 2: Da li Mediator može da pošalje poruku i dobije odgovor?
        String response = mediator.send(new InjectionRequest("Hello Poshtar"));

        assert response.equals("Request with Logged: Hello Poshtar") : "Odgovor nije ispravan!";
        System.out.println(">>> TEST PROŠAO: " + response);
    }

}