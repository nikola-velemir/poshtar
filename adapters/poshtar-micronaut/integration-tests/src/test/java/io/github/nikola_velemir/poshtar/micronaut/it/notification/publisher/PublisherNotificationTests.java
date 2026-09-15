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

package io.github.nikola_velemir.poshtar.micronaut.it.notification.publisher;

import io.github.nikola_velemir.poshtar.core.exceptions.AggregateNotificationException;
import io.github.nikola_velemir.poshtar.core.mediator.Publisher;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import io.github.nikola_velemir.poshtar.micronaut.adapter.internal.mediator.MicronautPoshtar;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.async.FailForAsyncFirstHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.async.FailForAsyncNotification;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.async.FailForAsyncSecondHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.async.FailForAsyncThirdHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.forget.FailForForgetFirstHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.forget.FailForForgetNotification;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.forget.FailForForgetSecondHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.forget.FailForForgetThirdHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.infrastructure.FailedExecutionNotification;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.infrastructure.FailedExecutionNotificationFineHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.injection.*;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.mock.*;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.noneRegistered.NoneRegisteredNotification;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.nullNotification.NullNotification;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.nullNotification.NullNotificationHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.ping.PingFirstHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.ping.PingNotification;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.ping.PingSecondHandler;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.transactional.basic.TransactionalNotification;
import io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.transactional.mandatory.MandatoryNotification;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import io.micronaut.context.annotation.Property;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.exceptions.IllegalTransactionStateException;
import io.micronaut.transaction.exceptions.NoTransactionException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * MOCKING STRATEGY IN THIS FILE
 * <p>
 * 1. Plain, non-AOP handlers/services below are mocked via a MANUALLY CONSTRUCTED raw instance
 * ({@code new X(...)}), never via an injected {@code target} bean parameter. This guarantees the
 * object {@code delegatesTo(...)} forwards to is a bare POJO that Micronaut's container has never
 * seen — it never passes through {@code BeanContext} resolution, any generated dispatch class, or
 * any interceptor chain. Accepting {@code target} as an injected parameter instead ties you to
 * however Micronaut happened to construct that particular bean, which — as observed with
 * {@code Publisher} in this suite, produced via a multi-argument {@code @Factory} method — can
 * itself trigger generated dispatch machinery that recurses against the Mockito mock
 * (StackOverflowError). Manual construction sidesteps that class of problem entirely.
 * <p>
 * 2. AOP-ADVISED HANDLERS (TransactionalNotificationFirstHandler, TransactionalNotificationSecondHandler,
 * MandatoryNotificationHandler, FailedExecutionNotificationHandler) are NOT mocked at all, by either
 * technique. Manual construction cannot be used for these: {@code new MandatoryNotificationHandler()}
 * gives you the raw, un-intercepted class, silently skipping the real {@code @Transactional} advice
 * that the test needs to exercise. These stay real, container-managed beans; behaviour is verified
 * via Publisher's thrown exceptions / notification state, not via {@code verify(...)} on the handler.
 * See: https://github.com/micronaut-projects/micronaut-core/discussions/11325
 * <p>
 * 3. {@code Publisher} itself is NOT mocked at all (see note above) — it's produced via a
 * multi-argument {@code @Factory} method, a combination documented as incompatible with
 * {@code @MockBean} overrides. It's injected directly as the real bean.
 */
@MicronautTest(rebuildContext = true, transactional = false)
@Property(name = "datasources.default.url", value = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
@Property(name = "datasources.default.driver-class-name", value = "org.h2.Driver")
@Property(name = "datasources.default.username", value = "sa")
@Property(name = "datasources.default.password", value = "")
@Property(name = "datasources.default.dialect", value = "H2")
@Property(name = "jpa.default.packages-to-scan", value = "io.github.nikola_velemir.poshtar")
@Property(name = "jpa.default.properties.hibernate.hbm2ddl.auto", value = "update")
@OverruleNoInjection
public class PublisherNotificationTests {

    @Inject
    Publisher publisher;

    @Inject
    MockService mockService;
    @Inject
    MockFirstNotificationHandler basicMockHandler;
    @Inject
    MockHierarchyNotificationHandler hierarchyNotificationHandler;
    @Inject
    MockServiceDeep mockServiceDeep;
    @Inject
    NullNotificationHandler nullNotificationHandler;
    @Inject
    PingFirstHandler pingFirstHandler;
    @Inject
    PingSecondHandler pingSecondHandler;
    @Inject
    InjectionNotificationFirstHandler injectionNotificationFirstHandler;
    @Inject
    InjectionNotificationSecondHandler injectionNotificationSecondHandler;
    @Inject
    InjectionNotificationThirdHandler injectionNotificationThirdHandler;
    @Inject
    DummyIncrementService dummyIncrementService;
    @Inject
    FailedExecutionNotificationFineHandler failedExecutionNotificationFineHandler;
    @Inject
    FailForAsyncFirstHandler failForAsyncFirstHandler;
    @Inject
    FailForAsyncSecondHandler failForAsyncSecondHandler;
    @Inject
    FailForAsyncThirdHandler failForAsyncThirdHandler;

    @Inject
    FailForForgetFirstHandler failForForgetFirstHandler;
    @Inject
    FailForForgetSecondHandler failForForgetSecondHandler;
    @Inject
    FailForForgetThirdHandler failForForgetThirdHandler;

    // --- Manually constructed raw instances — no BeanContext resolution, no dispatch class involved. ---
    // ASSUMPTION: no-arg constructors. Adjust if these classes actually take dependencies.
    @MockBean(Publisher.class)
    Publisher mockPublisher(NotificationRegistry notificationRegistry, RequestRegistry requestRegistry, @Named(TaskExecutors.IO) ExecutorService executorService) {
        return mock(Publisher.class, delegatesTo(new MicronautPoshtar(requestRegistry, notificationRegistry, executorService)));
    }

    @MockBean(MockFirstNotificationHandler.class)
    MockFirstNotificationHandler basicMockHandlerSpy(MockService mockService) {
        return mock(MockFirstNotificationHandler.class, delegatesTo(new MockFirstNotificationHandler(mockService)));
    }

    @MockBean(MockHierarchyNotificationHandler.class)
    MockHierarchyNotificationHandler hierarchyNotificationHandlerSpy(MockService mockService) {
        return mock(MockHierarchyNotificationHandler.class, delegatesTo(new MockHierarchyNotificationHandler(mockService)));
    }

    @MockBean(NullNotificationHandler.class)
    NullNotificationHandler nullNotificationHandlerSpy() {
        return mock(NullNotificationHandler.class, delegatesTo(new NullNotificationHandler()));
    }

    @MockBean(PingFirstHandler.class)
    PingFirstHandler pingFirstHandlerSpy() {
        return mock(PingFirstHandler.class, delegatesTo(new PingFirstHandler()));
    }

    @MockBean(PingSecondHandler.class)
    PingSecondHandler pingSecondHandlerSpy() {
        return mock(PingSecondHandler.class, delegatesTo(new PingSecondHandler()));
    }

    @MockBean(FailedExecutionNotificationFineHandler.class)
    FailedExecutionNotificationFineHandler failedExecutionNotificationFineHandlerSpy() {
        return mock(FailedExecutionNotificationFineHandler.class, delegatesTo(new FailedExecutionNotificationFineHandler()));
    }

    @MockBean(FailForAsyncFirstHandler.class)
    FailForAsyncFirstHandler failForAsyncFirstHandlerSpy() {
        return mock(FailForAsyncFirstHandler.class, delegatesTo(new FailForAsyncFirstHandler()));
    }

    @MockBean(FailForAsyncSecondHandler.class)
    FailForAsyncSecondHandler failForAsyncSecondHandlerSpy() {
        return mock(FailForAsyncSecondHandler.class, delegatesTo(new FailForAsyncSecondHandler()));
    }

    @MockBean(FailForAsyncThirdHandler.class)
    FailForAsyncThirdHandler failForAsyncThirdHandlerSpy() {
        return mock(FailForAsyncThirdHandler.class, delegatesTo(new FailForAsyncThirdHandler()));
    }


    @MockBean(MockService.class)
    MockService mockServiceMock() {
        return mock(MockService.class);
    }

    @MockBean(MockServiceDeep.class)
    MockServiceDeep mockServiceDeepMock() {
        return mock(MockServiceDeep.class);
    }

    @MockBean(DummyIncrementService.class)
    DummyIncrementService dummyIncrementServiceSpy() {
        return mock(DummyIncrementService.class, delegatesTo(new DummyIncrementService()));
    }

    @MockBean(InjectionNotificationFirstHandler.class)
    InjectionNotificationFirstHandler injectionNotificationFirstHandlerSpy(DummyIncrementService dummyIncrementService) {
        return mock(InjectionNotificationFirstHandler.class, delegatesTo(new InjectionNotificationFirstHandler(dummyIncrementService)));
    }

    @MockBean(InjectionNotificationSecondHandler.class)
    InjectionNotificationSecondHandler injectionNotificationSecondHandlerSpy(DummyIncrementService dummyIncrementService) {
        return mock(InjectionNotificationSecondHandler.class, delegatesTo(new InjectionNotificationSecondHandler(dummyIncrementService)));
    }

    @MockBean(InjectionNotificationThirdHandler.class)
    InjectionNotificationThirdHandler injectionNotificationThirdHandlerSpy(DummyIncrementService dummyIncrementService) {
        return mock(InjectionNotificationThirdHandler.class, delegatesTo(new InjectionNotificationThirdHandler(dummyIncrementService)));
    }

    @MockBean(FailForForgetFirstHandler.class)
    FailForForgetFirstHandler failForForgetFirstHandler() {
        return mock(FailForForgetFirstHandler.class, delegatesTo(new FailForForgetFirstHandler()));
    }

    @MockBean(FailForForgetSecondHandler.class)
    FailForForgetSecondHandler failForForgetSecondHandler() {
        return mock(FailForForgetSecondHandler.class, delegatesTo(new FailForForgetSecondHandler()));
    }

    @MockBean(FailForForgetThirdHandler.class)
    FailForForgetThirdHandler failForForgetThirdHandler() {
        return mock(FailForForgetThirdHandler.class, delegatesTo(new FailForForgetThirdHandler()));
    }
    @Test
    void should_Not_Fail_For_None_Registered() {
        var noneNotification = new NoneRegisteredNotification();
        assertDoesNotThrow(() -> publisher.publish(noneNotification));
        assertEquals(0, noneNotification.payload);

        verify(publisher, times(1)).publish(any());
        verify(publisher, times(1)).publish(eq(noneNotification));

    }

    @Test
    void handles_Null_Send() {
        NullNotification notification = null;
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> publisher.publish(notification));
        assertInstanceOf(IllegalArgumentException.class, ex);
        assertEquals("Request cannot be null", ex.getMessage());
        verify(nullNotificationHandler, never()).handle(eq(notification));

        verify(publisher, times(1)).publish(any());
        verify(publisher, times(1)).publish(eq(notification));
    }

    @Test
    void should_Stub_Basic() {
        var mockNotification = new MockNotification();

        when(mockService.getHello()).thenReturn("Bye");

        assertDoesNotThrow(() -> publisher.publish(mockNotification));
        assertEquals("Bye", mockNotification.getPayload());

        verify(mockService, times(1)).getHello();
        verify(basicMockHandler, times(1)).handle(eq(mockNotification));

        verify(publisher, times(1)).publish(any());
        verify(publisher, times(1)).publish(eq(mockNotification));
    }

    @Test
    void should_Stub_Hierarchy() {
        var mockNotification = new MockHierarchyNotification();

        when(mockServiceDeep.getHi()).thenReturn("Ciao");
        when(mockService.getHi()).then(i -> mockServiceDeep.getHi());
        assertDoesNotThrow(() -> publisher.publish(mockNotification));
        assertEquals("Ciao", mockNotification.getPayload());

        verify(mockService, times(1)).getHi();
        verify(mockServiceDeep, times(1)).getHi();
        verify(hierarchyNotificationHandler, times(1)).handle(eq(mockNotification));

        verify(publisher, times(1)).publish(any());
        verify(publisher, times(1)).publish(eq(mockNotification));
    }

    @Test
    void should_Register_And_Execute_Handler_Automatically() {
        PingNotification notification = new PingNotification();
        publisher.publish(notification);

        assertEquals(2, notification.payload);
        verify(pingFirstHandler, times(1)).handle(eq(notification));
        verify(pingSecondHandler, times(1)).handle(eq(notification));

        verify(publisher, times(1)).publish(any());
        verify(publisher, times(1)).publish(eq(notification));
    }

    @Test
    void should_Inject_Service_Into_Handlers() {
        InjectionNotification notification = new InjectionNotification();
        publisher.publish(notification);

        verify(injectionNotificationFirstHandler, times(1)).handle(any());
        verify(injectionNotificationSecondHandler, times(1)).handle(any());
        verify(injectionNotificationThirdHandler, times(1)).handle(any());
        verify(dummyIncrementService, times(3)).inc(anyInt());

        verify(publisher, times(1)).publish(any());

        verify(publisher, times(1)).publish(eq(notification));
    }

    /**
     * OPTION A: TransactionalNotificationFirstHandler/SecondHandler are real, un-mocked beans
     * (AOP-advised). We assert success via Publisher only — no verify(...) on the handlers.
     */
    @Test
    void should_Pass_For_Transactional() {
        var transactionNotification = new TransactionalNotification();
        assertDoesNotThrow(() -> publisher.publish(transactionNotification));

        verify(publisher, times(1)).publish(any());
        verify(publisher, times(1)).publish(eq(transactionNotification));
    }

    /**
     * OPTION A applied to MandatoryNotificationHandler: real bean, real @Transactional(MANDATORY)
     * check runs for real; assert on the thrown exception rather than verify(...).
     */
    @Test
    void should_Fail_For_Mandatory() {
        var mandatoryNotification = new MandatoryNotification();
        AggregateNotificationException mainEx = assertThrowsExactly(AggregateNotificationException.class, () -> publisher.publish(mandatoryNotification));

        Exception ex = (Exception) mainEx.getErrors().get(0);
        assertInstanceOf(IllegalTransactionStateException.class, ex);

        verify(publisher, times(1)).publish(any());
        verify(publisher, times(1)).publish(mandatoryNotification);

    }

    /**
     * OPTION A applied to FailedExecutionNotificationHandler: real bean, no verify(...) on it.
     * FailedExecutionNotificationFineHandler carries no AOP, so it's still mocked normally.
     */
    @Test
    void should_Fail_Purposefully_On_Execution() {
        var failNotification = new FailedExecutionNotification();

        AggregateNotificationException ex = assertThrowsExactly(AggregateNotificationException.class, () -> publisher.publish(failNotification));

        var errors = ex.getErrors();
        assertEquals(1, errors.size());
        assertInstanceOf(RuntimeException.class, errors.get(0));
        assertEquals(1, failNotification.payload);

        verify(failedExecutionNotificationFineHandler, times(1)).handle(eq(failNotification));
        verify(failedExecutionNotificationFineHandler, times(1)).handle(any());

        verify(publisher, times(1)).publish(any());
        verify(publisher, times(1)).publish(eq(failNotification));

    }

    @Test
    void should_Fail_For_Async() {
        var failAsyncNotification = new FailForAsyncNotification();
        AggregateNotificationException ex = assertThrowsExactly(AggregateNotificationException.class, () -> publisher.publish(failAsyncNotification));

        List<Throwable> errors = ex.getErrors();
        assertEquals(1, errors.size());
        assertInstanceOf(RuntimeException.class, errors.get(0));

        verify(failForAsyncSecondHandler, times(1)).handle(eq(failAsyncNotification));
        verify(failForAsyncFirstHandler, times(1)).handle(eq(failAsyncNotification));
        verify(failForAsyncThirdHandler, times(0)).handle(eq(failAsyncNotification));
        verify(failForAsyncThirdHandler, times(0)).handle(any());

        verify(publisher, times(1)).publish(any());
        verify(publisher, times(1)).publish(eq(failAsyncNotification));
    }
    @Test
    void should_Pass_For_Forget() throws InterruptedException {
        var notification = new FailForForgetNotification();
        assertDoesNotThrow(() -> publisher.publish(notification));

        Thread.sleep(1000);

        verify(failForForgetSecondHandler, times(1)).handle(eq(notification));
        verify(failForForgetFirstHandler, times(1)).handle(eq(notification));
        verify(failForForgetThirdHandler, times(0)).handle(eq(notification));
        verify(failForForgetThirdHandler, times(0)).handle(any());

        verify(publisher, times(1)).publish(any());
        verify(publisher, times(1)).publish(eq(notification));
    }
}