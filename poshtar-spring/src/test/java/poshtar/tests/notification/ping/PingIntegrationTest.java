package poshtar.tests.notification.ping;

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
    void should_Register_And_Execute_Handler_Automatically() {
        boolean firstBeanExists = context.containsBean(PingFirstHandler.class.getName());
        assert firstBeanExists : "Handler bean nije registrovan preko @RequestHandler!";
        boolean secondBeanExists = context.containsBean(PingSecondHandler.class.getName());
        assert secondBeanExists : "Handler bean nije registrovan preko @RequestHandler!";

        PingNotification notification = new PingNotification();
        mediator.publish(notification);

        assert notification.payload == 2;
        System.out.println(">>> TEST PROŠAO: ");
    }
}
