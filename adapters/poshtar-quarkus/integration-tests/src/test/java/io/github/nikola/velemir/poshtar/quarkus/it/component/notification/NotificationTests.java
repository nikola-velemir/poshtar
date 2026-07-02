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


import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.async.FailForAsyncFirstHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.async.FailForAsyncNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.async.FailForAsyncSecondHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.async.FailForAsyncThirdHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.infrastructure.FailedExecutionNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.infrastructure.FailedExecutionNotificationFineHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.infrastructure.FailedExecutionNotificationHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.injection.*;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.mock.*;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.noneRegistered.NoneRegisteredNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.nullNotification.NullNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.nullNotification.NullNotificationHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.ping.PingFirstHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.ping.PingNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.ping.PingSecondHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.transactional.basic.TransactionalNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.transactional.basic.TransactionalNotificationFirstHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.transactional.basic.TransactionalNotificationSecondHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.transactional.mandatory.MandatoryNotification;
import io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.transactional.mandatory.MandatoryNotificationHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.mock.basic.BasicMockRequestHandler;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.mock.hierarchy.HierarchyRequestHandler;
import io.github.nikola_velemir.poshtar.core.exceptions.AggregateNotificationException;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import jakarta.transaction.TransactionalException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@QuarkusTest
@OverruleNoInjection
public class NotificationTests {
    @InjectSpy
    Poshtar poshtar;

    @InjectMock
    MockService mockService;
    @InjectSpy
    MockFirstNotificationHandler basicMockHandler;
    @InjectSpy
    MockHierarchyNotificationHandler hierarchyNotificationHandler;
    @InjectMock
    MockServiceDeep mockServiceDeep;
    @InjectSpy
    NullNotificationHandler nullNotificationHandler;
    @InjectSpy
    PingFirstHandler pingFirstHandler;
    @InjectSpy
    PingSecondHandler pingSecondHandler;
    @InjectSpy
    InjectionNotificationFirstHandler injectionNotificationFirstHandler;
    @InjectSpy
    InjectionNotificationSecondHandler injectionNotificationSecondHandler;
    @InjectSpy
    InjectionNotificationThirdHandler injectionNotificationThirdHandler;
    @InjectSpy
    DummyIncrementService dummyIncrementService;
    @InjectSpy
    TransactionalNotificationFirstHandler transactionalNotificationFirstHandler;
    @InjectSpy
    TransactionalNotificationSecondHandler transactionalNotificationSecondHandler;

    @InjectSpy
    MandatoryNotificationHandler mandatoryNotificationHandler;
    @InjectSpy
    FailedExecutionNotificationHandler failedExecutionNotificationHandler;
    @InjectSpy
    FailedExecutionNotificationFineHandler failedExecutionNotificationFineHandler;
    @InjectSpy
    FailForAsyncFirstHandler failForAsyncFirstHandler;
    @InjectSpy
    FailForAsyncSecondHandler failForAsyncSecondHandler;
    @InjectSpy
    FailForAsyncThirdHandler failForAsyncThirdHandler;

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
        verify(nullNotificationHandler, never()).handle(eq(notification));

        verify(poshtar, times(1)).publish(eq(notification));
        verify(poshtar, times(1)).publish(any());
    }


    @Test
    void should_Stub_Basic() {
        var mockNotification = new MockNotification();

        when(mockService.getHello()).thenReturn("Bye");

        assertDoesNotThrow(() -> poshtar.publish(mockNotification));
        assertEquals("Bye", mockNotification.getPayload());

        verify(mockService, times(1)).getHello();
        verify(basicMockHandler, times(1)).handle(eq(mockNotification));
        verify(poshtar, times(1)).publish(eq(mockNotification));
    }

    @Test
    void should_Stub_Hierarchy() {
        var mockNotification = new MockHierarchyNotification();

        when(mockServiceDeep.getHi()).thenReturn("Ciao");
        when(mockService.getHi()).then(i -> mockServiceDeep.getHi());
        assertDoesNotThrow(() -> poshtar.publish(mockNotification));
        assertEquals("Ciao", mockNotification.getPayload());

        verify(mockService, times(1)).getHi();
        verify(mockServiceDeep, times(1)).getHi();
        verify(hierarchyNotificationHandler, times(1)).handle(eq(mockNotification));
        verify(poshtar, times(1)).publish(eq(mockNotification));
    }

    @Test
    void should_Register_And_Execute_Handler_Automatically() {

        PingNotification notification = new PingNotification();
        poshtar.publish(notification);

        assert notification.payload == 2;
        verify(pingFirstHandler, times(1)).handle(eq(notification));
        verify(pingSecondHandler, times(1)).handle(eq(notification));

        verify(poshtar, times(1)).publish(eq(notification));
        verify(poshtar, times(1)).publish(any());
        System.out.println(">>> TEST PASSED <<<");
    }

    @Test
    void should_Inject_Service_Into_Handlers() {

        InjectionNotification notification = new InjectionNotification();
        poshtar.publish(notification);

        verify(injectionNotificationFirstHandler, times(1)).handle(any());
        verify(injectionNotificationSecondHandler, times(1)).handle(any());
        verify(injectionNotificationThirdHandler, times(1)).handle(any());
        verify(dummyIncrementService, times(3)).inc(anyInt());

        verify(poshtar, times(1)).publish(eq(notification));
        verify(poshtar, times(1)).publish(any());
        System.out.println(">>> TEST PASSED <<<");
    }

    @Test
    void should_Pass_For_Transactional() {
        var transactionNotification = new TransactionalNotification();
        assertDoesNotThrow(() -> {
            poshtar.publish(transactionNotification);
        });
        verify(transactionalNotificationFirstHandler, times(1)).handle(eq(transactionNotification));
        verify(transactionalNotificationSecondHandler, times(1)).handle(eq(transactionNotification));

        verify(poshtar, times(1)).publish(eq(transactionNotification));
        verify(poshtar, times(1)).publish(any());
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
        verify(mandatoryNotificationHandler, times(1)).handle(eq(mandatoryNotification));
        verify(mandatoryNotificationHandler, times(1)).handle(any());

        verify(poshtar, times(1)).publish(eq(mandatoryNotification));
        verify(poshtar, times(1)).publish(any());
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
        System.out.println("Reads: " + errors.get(0).getClass().getName());
        assertInstanceOf(TransactionalException.class, errors.get(0));
        assertEquals(1, failNotification.payload);

        verify(failedExecutionNotificationHandler, times(1)).handle(eq(failNotification));
        verify(failedExecutionNotificationHandler, times(1)).handle(any());
        verify(failedExecutionNotificationFineHandler, times(1)).handle(eq(failNotification));
        verify(failedExecutionNotificationFineHandler, times(1)).handle(any());

        verify(poshtar, times(1)).publish(eq(failNotification));
        verify(poshtar, times(1)).publish(any());
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
        assertInstanceOf(RuntimeException.class, errors.get(0));


        verify(failForAsyncSecondHandler, times(1)).handle(eq(failAsyncNotification));
        verify(failForAsyncFirstHandler, times(1)).handle(eq(failAsyncNotification));
        verify(failForAsyncThirdHandler, times(1)).handle(eq(failAsyncNotification));
        verify(failForAsyncThirdHandler, times(1)).handle(any());


        verify(poshtar, times(1)).publish(eq(failAsyncNotification));
        verify(poshtar, times(1)).publish(any());
    }


}
