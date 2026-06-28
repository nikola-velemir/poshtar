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

package io.github.nikola.velemir.poshtar.quarkus.it.component.notification;


import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.async.FailForAsyncNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.infrastructure.FailedExecutionNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.injection.InjectionNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.noneRegistered.NoneRegisteredNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.nullNotification.NullNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.ping.PingFirstHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.ping.PingNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.transactional.basic.TransactionalNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.transactional.mandatory.MandatoryNotification;
import io.github.nikola_velemir.poshtar.core.exceptions.AggregateNotificationException;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.TransactionalException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
public class NotificationTests {
    @Inject
    Poshtar poshtar;

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

        PingNotification notification = new PingNotification();
        poshtar.publish(notification);

        assert notification.payload == 2;
        System.out.println(">>> TEST PASSED <<<");
    }

    @Test
    void should_Inject_Service_Into_Handlers() {

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
        System.out.println(mainEx);

        String expected = "ARJUNA016110: Transaction is required for invocation";
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
        assertInstanceOf(TransactionalException.class, errors.get(0));
        assertEquals(1, failNotification.payload);
        System.out.println(">>> TEST PASSED <<<");
    }

    @Test
    void should_Fail_For_Async() {
        var failAsyncNotification = new FailForAsyncNotification();
        AggregateNotificationException ex = assertThrowsExactly(
                AggregateNotificationException.class, () -> {
                    poshtar.publish(failAsyncNotification);

                });
        List<Throwable> errors = ex.getErrors();
        assertEquals(1, errors.size());
        assertInstanceOf(TransactionalException.class, errors.get(0));
    }
}
