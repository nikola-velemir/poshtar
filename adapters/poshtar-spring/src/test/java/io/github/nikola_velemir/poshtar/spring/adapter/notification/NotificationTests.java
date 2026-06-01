/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.spring.adapter.notification;

import io.github.nikola_velemir.poshtar.core.exceptions.AggregateNotificationException;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.IllegalTransactionStateException;
import io.github.nikola_velemir.poshtar.spring.adapter.MockTransactionConfig;
import io.github.nikola_velemir.poshtar.spring.adapter.TestApplication;
import io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.async.FailForAsyncNotification;
import io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.nullNotification.NullNotification;
import io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.infrastructure.FailedExecutionNotification;
import io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.injection.InjectionNotification;
import io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.injection.InjectionNotificationFirstHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.injection.InjectionNotificationSecondHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.injection.InjectionNotificationThirdHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.noneRegistered.NoneRegisteredNotification;
import io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.ping.PingFirstHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.ping.PingNotification;
import io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.ping.PingSecondHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.transactional.mandatory.MandatoryNotification;
import io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.transactional.basic.TransactionalNotification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestApplication.class})
@Import(MockTransactionConfig.class)
public class NotificationTests {
    @Autowired
    private Poshtar poshtar;
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
        Exception ex = (Exception) mainEx.getErrors().get(0);
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
        assertInstanceOf(IllegalTransactionStateException.class, errors.get(0));
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
        assertInstanceOf(IllegalTransactionStateException.class, errors.get(0));
    }
}
