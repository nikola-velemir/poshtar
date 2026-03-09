package poshtar.tests.notification;

import org.example.core.mediator.IMediator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.IllegalTransactionStateException;
import poshtar.tests.MockTransactionConfig;
import poshtar.tests.TestApplication;
import poshtar.tests.notification.deps.injection.InjectionNotification;
import poshtar.tests.notification.deps.injection.InjectionNotificationFirstHandler;
import poshtar.tests.notification.deps.injection.InjectionNotificationSecondHandler;
import poshtar.tests.notification.deps.injection.InjectionNotificationThirdHandler;
import poshtar.tests.notification.deps.ping.PingFirstHandler;
import poshtar.tests.notification.deps.ping.PingNotification;
import poshtar.tests.notification.deps.ping.PingSecondHandler;
import poshtar.tests.notification.deps.transactional.MandatoryNotification;
import poshtar.tests.notification.deps.transactional.TransactionalNotification;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestApplication.class})
@Import(MockTransactionConfig.class)
public class NotificationTests {
    @Autowired
    private IMediator mediator;
    @Autowired
    private ApplicationContext context;

    @Test
    void should_Register_And_Execute_Handler_Automatically() {
        boolean firstBeanExists = context.containsBean(PingFirstHandler.class.getName());
        assert firstBeanExists : "Handler bean not registered thru @RequestHandler!";
        boolean secondBeanExists = context.containsBean(PingSecondHandler.class.getName());
        assert secondBeanExists : "Handler bean not registered thru @RequestHandler!";

        PingNotification notification = new PingNotification();
        mediator.publish(notification);

        assert notification.payload == 2;
        System.out.println(">>> TEST PASSED <<<");
    }

    @Test
    void should_Inject_Service_Into_Handlers() {
        boolean firstBeanExists = context.containsBean(InjectionNotificationFirstHandler.class.getName());
        assert firstBeanExists : "Handler bean not registered thru @RequestHandler!";
        boolean secondBeanExists = context.containsBean(InjectionNotificationThirdHandler.class.getName());
        assert secondBeanExists : "Handler bean not registered thru @RequestHandler!";
        boolean thirdBeanExists = context.containsBean(InjectionNotificationSecondHandler.class.getName());
        assert thirdBeanExists : "Handler bean not registered thru @RequestHandler!";

        InjectionNotification notification = new InjectionNotification();
        mediator.publish(notification);

        assert notification.value == 3;
        System.out.println(">>> TEST PASSED <<<");
    }

    @Test
    void should_Pass_For_Transactional() {
        var transactionNotification = new TransactionalNotification();
        assertDoesNotThrow(() -> {
            mediator.publish(transactionNotification);
        });
        System.out.println(">>> TEST PASSED <<<");

    }

    @Test
    void should_Fail_For_Mandatory() {
        var mandatoryNotification = new MandatoryNotification();
        Exception ex = assertThrowsExactly(IllegalTransactionStateException.class, () -> {
            mediator.publish(mandatoryNotification);
        });
        String expected = "No existing transaction found for transaction marked with propagation 'mandatory'";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
    }
}
