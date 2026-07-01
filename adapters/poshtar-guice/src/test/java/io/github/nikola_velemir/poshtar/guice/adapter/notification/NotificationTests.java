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

package io.github.nikola_velemir.poshtar.guice.adapter.notification;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.infrastructure.FailedExecutionNotificationFineHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.infrastructure.FailedExecutionNotificationHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.injection.*;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.nullNotification.NullNotificationHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.ping.PingFirstHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.ping.PingSecondHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.transactional.fail.FailTransactionalNotificationFirstHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.transactional.fail.FailTransactionalNotificationSecondHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.transactional.sucess.TransactionalNotificationFirstHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.transactional.sucess.TransactionalNotificationSecondHandler;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.nikola_velemir.poshtar.core.exceptions.AggregateNotificationException;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.guice.adapter.TestModule;
import io.github.nikola_velemir.poshtar.guice.adapter.model.TestEntity;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.infrastructure.FailedExecutionNotification;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.noneRegistered.NoneRegisteredNotification;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.nullNotification.NullNotification;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.ping.PingNotification;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.transactional.fail.FailTransactionalNotification;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.transactional.sucess.TransactionalNotification;
import org.mockito.Mockito;


import java.util.List;

import static io.github.nikola_velemir.poshtar.guice.adapter.notification.NotificationTestsUtils.buildTestInjector;
import static io.github.nikola_velemir.poshtar.guice.adapter.notification.NotificationTestsUtils.createHandlerSpies;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class NotificationTests {
    private static Poshtar poshtar;
    private static Injector injector;
    static FailedExecutionNotificationHandler failedExecutionHandler;
    static FailedExecutionNotificationFineHandler failedExecutionFineHandler;
    static InjectionNotificationFirstHandler injectionFirstHandler;
    static InjectionNotificationSecondHandler injectionSecondHandler;
    static InjectionNotificationThirdHandler injectionThirdHandler;
    static DummyIncrementService dummyIncrementService;
    static NullNotificationHandler nullHandler;
    static PingFirstHandler pingFirstHandler;
    static PingSecondHandler pingSecondHandler;
    static FailTransactionalNotificationFirstHandler failTransactionalFirst;
    static FailTransactionalNotificationSecondHandler failTransactionalSecond;
    static TransactionalNotificationFirstHandler transactionalNotificationFirstHandler;
    static TransactionalNotificationSecondHandler transactionalNotificationSecondHandler;

    @BeforeEach
    void initTestContainer() {
        DummyIncrementService realIncrementService = new DummyIncrementService();
        dummyIncrementService = Mockito.spy(realIncrementService);

        Injector bootstrapInjector = Guice.createInjector(
                Modules.override(new TestModule()).with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(DummyIncrementService.class).toInstance(dummyIncrementService);
                    }
                })
        );

        createHandlerSpies(bootstrapInjector);

        Injector injector = buildTestInjector();

        poshtar = injector.getInstance(Poshtar.class);
    }


    @BeforeAll
    static void setUp() {
        injector = Guice.createInjector(new TestModule());

        poshtar = injector.getInstance(Poshtar.class);
    }

    @Test
    void should_Not_Fail_For_None_Registered() {
        var noneNotification = new NoneRegisteredNotification();
        assertDoesNotThrow(() -> poshtar.publish(noneNotification));
        assertEquals(0, noneNotification.payload);
    }

    @Test
    void should_Fail_For_Transactional() {
        var transactionNotification = new FailTransactionalNotification("Fail Pass");
        Exception ex = assertThrowsExactly(AggregateNotificationException.class, () -> {
            poshtar.publish(transactionNotification);


        });

        EntityManager em = injector.getInstance(EntityManager.class);
        em.getTransaction().begin();
        List<TestEntity> results = em.createQuery("SELECT d FROM TestEntity d where d.data = 'First Fail Pass'", TestEntity.class).getResultList();
        em.getTransaction().commit();
        assertFalse(results.isEmpty(), "Transaction did not commit!.");
        assertEquals(1, results.size());

        verify(failTransactionalFirst, times(1)).handle(eq(transactionNotification));
        verify(failTransactionalSecond, times(1)).handle(eq(transactionNotification));
        System.out.println(">>> TEST PASSED <<<");

    }

    @Test
    void should_Pass_For_Transactional() {
        var transactionNotification = new TransactionalNotification("Pass");
        assertDoesNotThrow(() -> {
            poshtar.publish(transactionNotification);

            EntityManager em = injector.getInstance(EntityManager.class);
            em.getTransaction().begin();
            List<TestEntity> results = em.createQuery("SELECT d FROM TestEntity d where d.data = 'First Pass'", TestEntity.class).getResultList();
            em.getTransaction().commit();
            assertFalse(results.isEmpty(), "Transaction did not commit!.");

            em.getTransaction().begin();
            results = em.createQuery("SELECT d FROM TestEntity d where d.data = 'Second Pass'", TestEntity.class).getResultList();
            em.getTransaction().commit();
            assertFalse(results.isEmpty(), "Transaction did not commit!.");

        });

        verify(transactionalNotificationFirstHandler, times(1)).handle(eq(transactionNotification));
        verify(transactionalNotificationSecondHandler, times(1)).handle(eq(transactionNotification));

        System.out.println(">>> TEST PASSED <<<");

    }

    @Test
    void handles_Null_Send() {
        NullNotification notification = null;
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> poshtar.publish(notification));
        assertInstanceOf(IllegalArgumentException.class, ex);
        String expected = "Request cannot be null";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
        verify(nullHandler, never()).handle(eq(notification));
        verify(nullHandler, never()).handle(any());
    }

    @Test
    void should_Register_And_Execute_Handler_Automatically() {


        PingNotification notification = new PingNotification();
        poshtar.publish(notification);


        verify(pingFirstHandler, times(1)).handle(eq(notification));
        verify(pingSecondHandler, times(1)).handle(eq(notification));

        System.out.println(">>> TEST PASSED <<<");
    }

    @Test
    void should_Inject_Service_Into_Handlers() {

        InjectionNotification notification = new InjectionNotification();
        assertDoesNotThrow(() -> {

            poshtar.publish(notification);

            assert notification.value == 3;

            verify(injectionFirstHandler, times(1)).handle(eq(notification));

            verify(injectionSecondHandler, times(1)).handle(eq(notification));

            verify(injectionThirdHandler, times(1)).handle(eq(notification));

            System.out.println(">>> TEST PASSED <<<");
        });
    }


    @Test
    void should_Fail_Purposefully_On_Execution() {
        var failNotification = new FailedExecutionNotification();

        AggregateNotificationException ex = assertThrowsExactly(AggregateNotificationException.class, () -> poshtar.publish(failNotification));
        var errors = ex.getErrors();
        assertEquals(1, errors.size());
        assertInstanceOf(RuntimeException.class, errors.get(0));
        assertEquals(1, failNotification.payload);
        System.out.println(">>> TEST PASSED <<<");
        verify(failedExecutionHandler, times(1)).handle(eq(failNotification));
        verify(failedExecutionFineHandler, times(1)).handle(eq(failNotification));
    }

}
