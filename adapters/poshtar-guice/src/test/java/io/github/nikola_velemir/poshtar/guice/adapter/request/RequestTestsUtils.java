package io.github.nikola_velemir.poshtar.guice.adapter.request;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import io.github.nikola_velemir.poshtar.guice.adapter.TestModule;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining.ChainingFirstRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining.ChainingSecondRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.DummyLoggingService;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.InjectionRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.mock.MockRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.nullRequest.NullRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.ping.PingRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.fail.FailForTransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.TransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.UpdateTransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import org.mockito.Mockito;

/**
 * Shared test bootstrap helpers for the Poshtar request test suites.
 * <p>
 * MUST be public (and its methods public): it is used from {@code request.sender}
 * and {@code request.mediator}, which are sibling sub-packages, not the same
 * package as this class. Package-private access does not extend across
 * sub-packages in Java, so the previous package-private version failed to
 * compile from those callers.
 * <p>
 * Also intentionally NOT coupled to any specific {@code RequestTests} class.
 * The previous version wrote directly to static fields on one hardcoded
 * {@code RequestTests} (the {@code sender} one), which silently left any
 * *other* test class's fields (e.g. {@code mediator.RequestTests}) null,
 * causing NullPointerExceptions the moment those tests stubbed or verified
 * a handler. {@link #createMocks()} and {@link #createSpies} now return
 * plain holder objects; each calling test class copies what it needs into
 * its own static fields.
 */
@OverruleNoInjection
public class RequestTestsUtils {

    /** Plain Mockito mocks shared by a single test run. */
    public static class Mocks {
        public MockRequestHandler mockRequestHandler;
    }

    /** Mockito spies wrapping real Guice-created handler instances. */
    public static class Spies {
        public DummyLoggingService dummyLoggingService;
        public NullRequestHandler nullRequestHandler;
        public PingRequestHandler pingRequestHandler;
        public InjectionRequestHandler injectionRequestHandler;
        public TransactionalRequestHandler transactionalRequestHandler;
        public UpdateTransactionalRequestHandler updateTransactionalRequestHandler;
        public FailForTransactionalRequestHandler failForTransactionalRequestHandler;
        public ChainingFirstRequestHandler chainingFirstRequestHandler;
        public ChainingSecondRequestHandler chainingSecondRequestHandler;
    }

    public static Mocks createMocks() {
        Mocks mocks = new Mocks();
        mocks.mockRequestHandler = Mockito.mock(MockRequestHandler.class);
        return mocks;
    }

    /** Creates a spy of the real DummyLoggingService, resolved from a bare TestModule injector. */
    public static DummyLoggingService createLoggingSpy() {
        Injector bootstrapInjector = Guice.createInjector(new TestModule());
        DummyLoggingService realLoggingService = bootstrapInjector.getInstance(DummyLoggingService.class);
        return Mockito.spy(realLoggingService);
    }

    /** Bootstrap injector with only the logging spy bound, used to resolve real handler instances to wrap as spies. */
    public static Injector buildHandlerInjector(DummyLoggingService dummyLoggingService) {
        return Guice.createInjector(
                Modules.override(new TestModule()).with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(DummyLoggingService.class).toInstance(dummyLoggingService);
                    }
                })
        );
    }

    public static Spies createSpies(Injector handlerInjector, DummyLoggingService dummyLoggingService) {
        Spies spies = new Spies();
        spies.dummyLoggingService = dummyLoggingService;
        spies.nullRequestHandler = Mockito.spy(handlerInjector.getInstance(NullRequestHandler.class));
        spies.pingRequestHandler = Mockito.spy(handlerInjector.getInstance(PingRequestHandler.class));
        spies.injectionRequestHandler = Mockito.spy(handlerInjector.getInstance(InjectionRequestHandler.class));
        spies.transactionalRequestHandler = Mockito.spy(handlerInjector.getInstance(TransactionalRequestHandler.class));
        spies.updateTransactionalRequestHandler = Mockito.spy(handlerInjector.getInstance(UpdateTransactionalRequestHandler.class));
        spies.failForTransactionalRequestHandler = Mockito.spy(handlerInjector.getInstance(FailForTransactionalRequestHandler.class));
        spies.chainingFirstRequestHandler = Mockito.spy(handlerInjector.getInstance(ChainingFirstRequestHandler.class));
        spies.chainingSecondRequestHandler = Mockito.spy(handlerInjector.getInstance(ChainingSecondRequestHandler.class));
        return spies;
    }

    public static Injector buildTestInjector(Mocks mocks, Spies spies) {
        return Guice.createInjector(Modules.override(new TestModule()).with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(MockRequestHandler.class).toInstance(mocks.mockRequestHandler);
                bind(DummyLoggingService.class).toInstance(spies.dummyLoggingService);
                bind(NullRequestHandler.class).toInstance(spies.nullRequestHandler);
                bind(PingRequestHandler.class).toInstance(spies.pingRequestHandler);

                bind(InjectionRequestHandler.class).toInstance(spies.injectionRequestHandler);

                bind(TransactionalRequestHandler.class).toInstance(spies.transactionalRequestHandler);
                bind(UpdateTransactionalRequestHandler.class).toInstance(spies.updateTransactionalRequestHandler);
                bind(FailForTransactionalRequestHandler.class).toInstance(spies.failForTransactionalRequestHandler);

                bind(ChainingFirstRequestHandler.class).toInstance(spies.chainingFirstRequestHandler);
                bind(ChainingSecondRequestHandler.class).toInstance(spies.chainingSecondRequestHandler);
            }
        }));
    }
}