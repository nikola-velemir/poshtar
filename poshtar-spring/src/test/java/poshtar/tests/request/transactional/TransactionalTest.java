package poshtar.tests.request.transactional;

import adapter.EnablePoshtar;
import org.example.core.mediator.IMediator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import poshtar.tests.TestApplication;
@SpringBootTest(classes = TestApplication.class)
public class TransactionalTest {
    @Autowired
    private IMediator mediator;
    @Autowired
    private ApplicationContext context;
    @Test
    void should_Register_And_Execute_Handler_Automatically() throws Exception {
        boolean beanExists = context.containsBean(TransactionalRequestHandler.class.getName());
        assert beanExists : "Handler bean nije registrovan preko @RequestHandler!";
         String response = mediator.send(new TransactionalRequest("Hello Poshtar"));

        assert response.equals("Request with Hello Poshtar") : "Odgovor nije ispravan!";
        System.out.println(">>> TEST PROŠAO: " + response);
    }
}
