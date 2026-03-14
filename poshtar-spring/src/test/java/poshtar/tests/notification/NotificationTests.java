package poshtar.tests.notification;

import org.example.core.exceptions.AggregateNotificationException;
import org.example.core.mediator.IPoshtar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.IllegalTransactionStateException;
import poshtar.tests.MockTransactionConfig;
import poshtar.tests.TestApplication;
import poshtar.tests.notification.deps.async.FailForAsyncNotification;
import poshtar.tests.notification.deps.nullNotification.NullNotification;
import poshtar.tests.notification.deps.infrastructure.FailedExecutionNotification;
import poshtar.tests.notification.deps.injection.InjectionNotification;
import poshtar.tests.notification.deps.injection.InjectionNotificationFirstHandler;
import poshtar.tests.notification.deps.injection.InjectionNotificationSecondHandler;
import poshtar.tests.notification.deps.injection.InjectionNotificationThirdHandler;
import poshtar.tests.notification.deps.noneRegistered.NoneRegisteredNotification;
import poshtar.tests.notification.deps.ping.PingFirstHandler;
import poshtar.tests.notification.deps.ping.PingNotification;
import poshtar.tests.notification.deps.ping.PingSecondHandler;
import poshtar.tests.notification.deps.transactional.MandatoryNotification;
import poshtar.tests.notification.deps.transactional.TransactionalNotification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestApplication.class})
@Import(MockTransactionConfig.class)
public class NotificationTests {
    @Autowired
    private IPoshtar poshtar;
    @Autowired
    private ApplicationContext context;

    @Test
    void should_Not_Fail_For_None_Registered() {
        var noneNotification = new NoneRegisteredNotification();
        assertDoesNotThrow(() -> {
            poshtar.publish(noneNotification);
        });
        assertEquals(0, noneNotification.payload);
    }

    @Test
    void handles_Null_Send() {
        NullNotification notification = null;
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> {
            poshtar.publish(notification);
        });
        assertInstanceOf(IllegalArgumentException.class, ex);
        String expected = "Request cannot be null";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    void should_Register_And_Execute_Handler_Automatically() {
        boolean firstBeanExists = context.containsBean(PingFirstHandler.class.getName());
        assert firstBeanExists : "Handler bean not registered thru @NotificationHandler!";
        boolean secondBeanExists = context.containsBean(PingSecondHandler.class.getName());
        assert secondBeanExists : "Handler bean not registered thru @NotificationHandler!";

        PingNotification notification = new PingNotification();
        poshtar.publish(notification);

        assert notification.payload == 2;
        System.out.println(">>> TEST PASSED <<<");
    }

    @Test
    void should_Inject_Service_Into_Handlers() {
        boolean firstBeanExists = context.containsBean(InjectionNotificationFirstHandler.class.getName());
        assert firstBeanExists : "Handler bean not registered thru @NotificationHandler!";
        boolean secondBeanExists = context.containsBean(InjectionNotificationThirdHandler.class.getName());
        assert secondBeanExists : "Handler bean not registered thru @NotificationHandler!";
        boolean thirdBeanExists = context.containsBean(InjectionNotificationSecondHandler.class.getName());
        assert thirdBeanExists : "Handler bean not registered thru @NotificationHandler!";

        InjectionNotification notification = new InjectionNotification();
        poshtar.publish(notification);

        assert notification.value == 3;
        System.out.println(">>> TEST PASSED <<<");
    }

    @Test
    void should_Pass_For_Transactional() {
        var transactionNotification = new TransactionalNotification();
        assertDoesNotThrow(() -> {
            poshtar.publish(transactionNotification);
        });
        System.out.println(">>> TEST PASSED <<<");

    }

    @Test
    void should_Fail_For_Mandatory() {
        var mandatoryNotification = new MandatoryNotification();
        AggregateNotificationException mainEx = assertThrowsExactly(AggregateNotificationException.class, () -> {
            poshtar.publish(mandatoryNotification);
        });
        Exception ex = (Exception) mainEx.getErrors().getFirst();
        String expected = "No existing transaction found for transaction marked with propagation 'mandatory'";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
        System.out.println(">>> TEST PASSED <<<");
    }

    @Test
    void should_Fail_Purposefully_On_Execution() {
        var failNotification = new FailedExecutionNotification();

        AggregateNotificationException ex = assertThrowsExactly(AggregateNotificationException.class, () -> {
            poshtar.publish(failNotification);
        });
        var errors = ex.getErrors();
        assertEquals(1, errors.size());
        assertInstanceOf(IllegalTransactionStateException.class, errors.getFirst());
        assertEquals(1, failNotification.payload);
        System.out.println(">>> TEST PASSED <<<");
    }

    @Test
    void should_Fail_For_Async() {
        var failAsyncNotification = new FailForAsyncNotification();
        AggregateNotificationException ex = assertThrowsExactly(
                AggregateNotificationException.class, () -> {
                    poshtar.publish(failAsyncNotification);

                }
        );
        List<Throwable> errors = ex.getErrors();
        assertEquals(1, errors.size());
        assertInstanceOf(IllegalTransactionStateException.class, errors.getFirst());
    }
}
