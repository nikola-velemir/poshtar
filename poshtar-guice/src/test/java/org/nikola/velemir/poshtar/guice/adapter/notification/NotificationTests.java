package org.nikola.velemir.poshtar.guice.adapter.notification;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nikola.velemir.poshtar.core.exceptions.AggregateNotificationException;
import org.nikola.velemir.poshtar.core.mediator.Poshtar;
import org.nikola.velemir.poshtar.guice.adapter.TestModule;
import org.nikola.velemir.poshtar.guice.adapter.notification.deps.infrastructure.FailedExecutionNotification;
import org.nikola.velemir.poshtar.guice.adapter.notification.deps.injection.InjectionNotification;
import org.nikola.velemir.poshtar.guice.adapter.notification.deps.noneRegistered.NoneRegisteredNotification;
import org.nikola.velemir.poshtar.guice.adapter.notification.deps.nullNotification.NullNotification;
import org.nikola.velemir.poshtar.guice.adapter.notification.deps.ping.PingNotification;


import static org.junit.jupiter.api.Assertions.*;

public class NotificationTests {
    private static Poshtar poshtar;

    @BeforeAll
    static void setUp() {
        Injector injector = Guice.createInjector(new TestModule());

        poshtar = injector.getInstance(Poshtar.class);
    }

    @Test
    void should_Not_Fail_For_None_Registered() {
        var noneNotification = new NoneRegisteredNotification();
        assertDoesNotThrow(() -> poshtar.publish(noneNotification));
        assertEquals(0, noneNotification.payload);
    }

    @Test
    void handles_Null_Send() {
        NullNotification notification = null;
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> poshtar.publish(notification));
        assertInstanceOf(IllegalArgumentException.class, ex);
        String expected = "Request cannot be null";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    void should_Register_And_Execute_Handler_Automatically() {


        PingNotification notification = new PingNotification();
        poshtar.publish(notification);

        assert notification.payload == 2;
        System.out.println(">>> TEST PASSED <<<");
    }

    @Test
    void should_Inject_Service_Into_Handlers() {

        InjectionNotification notification = new InjectionNotification();
        assertDoesNotThrow(()->{

            poshtar.publish(notification);

            assert notification.value == 3;
            System.out.println(">>> TEST PASSED <<<");
        });
    }



    @Test
    void should_Fail_Purposefully_On_Execution() {
        var failNotification = new FailedExecutionNotification();

        AggregateNotificationException ex = assertThrowsExactly(AggregateNotificationException.class, () -> poshtar.publish(failNotification));
        var errors = ex.getErrors();
        assertEquals(1, errors.size());
        assertInstanceOf(RuntimeException.class, errors.getFirst());
        assertEquals(1, failNotification.payload);
        System.out.println(">>> TEST PASSED <<<");
    }

}
