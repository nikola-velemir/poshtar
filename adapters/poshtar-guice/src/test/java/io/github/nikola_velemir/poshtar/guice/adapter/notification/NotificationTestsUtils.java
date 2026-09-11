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

/**
 * Shared test bootstrap helpers for the Poshtar notification test suites.
 * <p>
 * IMPORTANT: this class is intentionally NOT coupled to any specific
 * {@code NotificationTests} class. Earlier versions wrote directly to
 * static fields on one hardcoded {@code NotificationTests} class, which
 * silently broke any *other* test class (e.g. one in a sibling package)
 * that reused these helpers - their fields were never populated and Guice
 * failed with "Binding to null instances is not allowed".
 * <p>
 * Instead, {@link #createMocks()} and {@link #createSpies(Injector)} return
 * plain holder objects. Each calling test class is responsible for copying
 * the fields it needs into its own static fields.
 */
@OverruleNoInjection
public class NotificationTestsUtils {

    /** Plain Mockito mocks shared by a single test run. */
    public static class Mocks {
        public MockService mockService;
        public MockServiceDeep mockServiceDeep;
    }

    /** Mockito spies wrapping real Guice-created handler instances. */
    public static class Spies {
        public FailedExecutionNotificationHandler failedExecutionHandler;
        public FailedExecutionNotificationFineHandler failedExecutionFineHandler;
        public InjectionNotificationFirstHandler injectionFirstHandler;
        public InjectionNotificationSecondHandler injectionSecondHandler;
        public InjectionNotificationThirdHandler injectionThirdHandler;
        public NullNotificationHandler nullHandler;
        public PingFirstHandler pingFirstHandler;
        public PingSecondHandler pingSecondHandler;
        public FailTransactionalNotificationFirstHandler failTransactionalFirst;
        public FailTransactionalNotificationSecondHandler failTransactionalSecond;
        public TransactionalNotificationFirstHandler transactionalNotificationFirstHandler;
        public TransactionalNotificationSecondHandler transactionalNotificationSecondHandler;
        public BasicMockNotificationHandler basicMockHandler;
        public MockHierarchyNotificationHandler hierarchyNotificationHandler;
    }

    public static Mocks createMocks() {
        Mocks mocks = new Mocks();
        mocks.mockService = Mockito.mock(MockService.class);
        mocks.mockServiceDeep = Mockito.mock(MockServiceDeep.class);
        return mocks;
    }

    public static Spies createSpies(Injector bootstrapInjector) {
        Spies spies = new Spies();
        spies.failedExecutionHandler = Mockito.spy(bootstrapInjector.getInstance(FailedExecutionNotificationHandler.class));
        spies.failedExecutionFineHandler = Mockito.spy(bootstrapInjector.getInstance(FailedExecutionNotificationFineHandler.class));
        spies.injectionFirstHandler = Mockito.spy(bootstrapInjector.getInstance(InjectionNotificationFirstHandler.class));
        spies.injectionSecondHandler = Mockito.spy(bootstrapInjector.getInstance(InjectionNotificationSecondHandler.class));
        spies.injectionThirdHandler = Mockito.spy(bootstrapInjector.getInstance(InjectionNotificationThirdHandler.class));
        spies.nullHandler = Mockito.spy(bootstrapInjector.getInstance(NullNotificationHandler.class));
        spies.pingFirstHandler = Mockito.spy(bootstrapInjector.getInstance(PingFirstHandler.class));
        spies.pingSecondHandler = Mockito.spy(bootstrapInjector.getInstance(PingSecondHandler.class));
        spies.failTransactionalFirst = Mockito.spy(bootstrapInjector.getInstance(FailTransactionalNotificationFirstHandler.class));
        spies.failTransactionalSecond = Mockito.spy(bootstrapInjector.getInstance(FailTransactionalNotificationSecondHandler.class));
        spies.transactionalNotificationFirstHandler = Mockito.spy(bootstrapInjector.getInstance(TransactionalNotificationFirstHandler.class));
        spies.transactionalNotificationSecondHandler = Mockito.spy(bootstrapInjector.getInstance(TransactionalNotificationSecondHandler.class));
        spies.basicMockHandler = Mockito.spy(bootstrapInjector.getInstance(BasicMockNotificationHandler.class));
        spies.hierarchyNotificationHandler = Mockito.spy(bootstrapInjector.getInstance(MockHierarchyNotificationHandler.class));
        return spies;
    }

    public static Injector buildTestInjector(DummyIncrementService dummyIncrementService, Mocks mocks, Spies spies) {
        return Guice.createInjector(
                Modules.override(new TestModule()).with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(DummyIncrementService.class).toInstance(dummyIncrementService);
                        bind(FailedExecutionNotificationHandler.class).toInstance(spies.failedExecutionHandler);
                        bind(FailedExecutionNotificationFineHandler.class).toInstance(spies.failedExecutionFineHandler);
                        bind(InjectionNotificationFirstHandler.class).toInstance(spies.injectionFirstHandler);
                        bind(InjectionNotificationSecondHandler.class).toInstance(spies.injectionSecondHandler);
                        bind(InjectionNotificationThirdHandler.class).toInstance(spies.injectionThirdHandler);
                        bind(NullNotificationHandler.class).toInstance(spies.nullHandler);
                        bind(PingFirstHandler.class).toInstance(spies.pingFirstHandler);
                        bind(PingSecondHandler.class).toInstance(spies.pingSecondHandler);
                        bind(FailTransactionalNotificationSecondHandler.class).toInstance(spies.failTransactionalSecond);
                        bind(FailTransactionalNotificationFirstHandler.class).toInstance(spies.failTransactionalFirst);
                        bind(TransactionalNotificationSecondHandler.class).toInstance(spies.transactionalNotificationSecondHandler);
                        bind(TransactionalNotificationFirstHandler.class).toInstance(spies.transactionalNotificationFirstHandler);
                        bind(BasicMockNotificationHandler.class).toInstance(spies.basicMockHandler);
                        bind(MockHierarchyNotificationHandler.class).toInstance(spies.hierarchyNotificationHandler);
                        bind(MockServiceDeep.class).toInstance(mocks.mockServiceDeep);
                        bind(MockService.class).toInstance(mocks.mockService);
                    }
                })
        );
    }
}