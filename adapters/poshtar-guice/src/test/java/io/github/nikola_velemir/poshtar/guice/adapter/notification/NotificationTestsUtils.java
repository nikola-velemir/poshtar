package io.github.nikola_velemir.poshtar.guice.adapter.notification;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import io.github.nikola_velemir.poshtar.guice.adapter.TestModule;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.infrastructure.FailedExecutionNotificationFineHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.infrastructure.FailedExecutionNotificationHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.injection.DummyIncrementService;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.injection.InjectionNotificationFirstHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.injection.InjectionNotificationSecondHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.injection.InjectionNotificationThirdHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.mock.*;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.nullNotification.NullNotificationHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.ping.PingFirstHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.ping.PingSecondHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.transactional.fail.FailTransactionalNotificationFirstHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.transactional.fail.FailTransactionalNotificationSecondHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.transactional.sucess.TransactionalNotificationFirstHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.transactional.sucess.TransactionalNotificationSecondHandler;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import org.mockito.Mockito;

@OverruleNoInjection
public class NotificationTestsUtils {
    static void createMocks() {
        NotificationTests.mockService = Mockito.mock(MockService.class);
        NotificationTests.mockServiceDeep = Mockito.mock(MockServiceDeep.class);
    }

    static void createSpies(Injector bootstrapInjector) {
        NotificationTests.failedExecutionHandler = Mockito.spy(bootstrapInjector.getInstance(FailedExecutionNotificationHandler.class));
        NotificationTests.failedExecutionFineHandler = Mockito.spy(bootstrapInjector.getInstance(FailedExecutionNotificationFineHandler.class));
        NotificationTests.injectionFirstHandler = Mockito.spy(bootstrapInjector.getInstance(InjectionNotificationFirstHandler.class));
        NotificationTests.injectionSecondHandler = Mockito.spy(bootstrapInjector.getInstance(InjectionNotificationSecondHandler.class));
        NotificationTests.injectionThirdHandler = Mockito.spy(bootstrapInjector.getInstance(InjectionNotificationThirdHandler.class));
        NotificationTests.nullHandler = Mockito.spy(bootstrapInjector.getInstance(NullNotificationHandler.class));
        NotificationTests.pingFirstHandler = Mockito.spy(bootstrapInjector.getInstance(PingFirstHandler.class));
        NotificationTests.pingSecondHandler = Mockito.spy(bootstrapInjector.getInstance(PingSecondHandler.class));
        NotificationTests.failTransactionalFirst = Mockito.spy(bootstrapInjector.getInstance(FailTransactionalNotificationFirstHandler.class));
        NotificationTests.failTransactionalSecond = Mockito.spy(bootstrapInjector.getInstance(FailTransactionalNotificationSecondHandler.class));
        NotificationTests.transactionalNotificationFirstHandler = Mockito.spy(bootstrapInjector.getInstance(TransactionalNotificationFirstHandler.class));
        NotificationTests.transactionalNotificationSecondHandler = Mockito.spy(bootstrapInjector.getInstance(TransactionalNotificationSecondHandler.class));

        NotificationTests.basicMockHandler = Mockito.spy(bootstrapInjector.getInstance(BasicMockNotificationHandler.class));
        NotificationTests.hierarchyNotificationHandler = Mockito.spy(bootstrapInjector.getInstance(MockHierarchyNotificationHandler.class));

    }

    static Injector buildTestInjector() {
        return Guice.createInjector(
                Modules.override(new TestModule()).with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(DummyIncrementService.class).toInstance(NotificationTests.dummyIncrementService);
                        bind(FailedExecutionNotificationHandler.class).toInstance(NotificationTests.failedExecutionHandler);
                        bind(FailedExecutionNotificationFineHandler.class).toInstance(NotificationTests.failedExecutionFineHandler);
                        bind(InjectionNotificationFirstHandler.class).toInstance(NotificationTests.injectionFirstHandler);
                        bind(InjectionNotificationSecondHandler.class).toInstance(NotificationTests.injectionSecondHandler);
                        bind(InjectionNotificationThirdHandler.class).toInstance(NotificationTests.injectionThirdHandler);
                        bind(NullNotificationHandler.class).toInstance(NotificationTests.nullHandler);
                        bind(PingFirstHandler.class).toInstance(NotificationTests.pingFirstHandler);
                        bind(PingSecondHandler.class).toInstance(NotificationTests.pingSecondHandler);
                        bind(FailTransactionalNotificationSecondHandler.class).toInstance(NotificationTests.failTransactionalSecond);
                        bind(FailTransactionalNotificationFirstHandler.class).toInstance(NotificationTests.failTransactionalFirst);
                        bind(TransactionalNotificationSecondHandler.class).toInstance(NotificationTests.transactionalNotificationSecondHandler);
                        bind(TransactionalNotificationFirstHandler.class).toInstance(NotificationTests.transactionalNotificationFirstHandler);
                        bind(BasicMockNotificationHandler.class).toInstance(NotificationTests.basicMockHandler);
                        bind(MockHierarchyNotificationHandler.class).toInstance(NotificationTests.hierarchyNotificationHandler);
                        bind(MockServiceDeep.class).toInstance(NotificationTests.mockServiceDeep);
                        bind(MockService.class).toInstance(NotificationTests.mockService);
                    }
                })
        );
    }
}
