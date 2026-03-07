package poshtar.tests.notification.injection;

import org.example.core.mediator.IMediator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import poshtar.tests.TestApplication;
import poshtar.tests.notification.ping.PingFirstHandler;
import poshtar.tests.notification.ping.PingNotification;
import poshtar.tests.notification.ping.PingSecondHandler;

@SpringBootTest(classes = {TestApplication.class})
public class InjectionIntegrationTest {
    @Autowired
    private IMediator mediator;
    @Autowired
    private ApplicationContext context;
    @Test
    void should_Register_And_Execute_Handler_Automatically() {
        boolean firstBeanExists = context.containsBean(InjectionNotificationFirstHandler.class.getName());
        assert firstBeanExists : "Handler bean nije registrovan preko @RequestHandler!";
        boolean secondBeanExists = context.containsBean(InjectionNotificationThirdHandler.class.getName());
        assert secondBeanExists : "Handler bean nije registrovan preko @RequestHandler!";
        boolean thirdBeanExists = context.containsBean(InjectionNotificationSecondHandler.class.getName());
        assert thirdBeanExists : "Handler bean nije registrovan preko @RequestHandler!";

        InjectionNotification notification = new InjectionNotification();
        mediator.publish(notification);

        assert notification.value == 3;
        System.out.println(">>> TEST PROŠAO: ");
    }

}
