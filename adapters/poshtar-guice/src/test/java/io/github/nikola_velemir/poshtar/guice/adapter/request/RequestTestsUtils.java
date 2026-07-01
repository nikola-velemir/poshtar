package io.github.nikola_velemir.poshtar.guice.adapter.request;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import io.github.nikola_velemir.poshtar.guice.adapter.TestModule;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.DummyLoggingService;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.InjectionRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.nullRequest.NullRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.ping.PingRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.fail.FailForTransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.TransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.UpdateTransactionalRequestHandler;
import org.mockito.Mockito;

class RequestTestsUtils {
    static Injector buildTestInjector() {
        return Guice.createInjector(
                Modules.override(new TestModule()).with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(DummyLoggingService.class).toInstance(RequestTests.dummyLoggingService);
                        bind(NullRequestHandler.class).toInstance(RequestTests.nullRequestHandler);
                        bind(PingRequestHandler.class).toInstance(RequestTests.pingRequestHandler);
                        bind(InjectionRequestHandler.class).toInstance(RequestTests.injectionRequestHandler);
                        bind(TransactionalRequestHandler.class).toInstance(RequestTests.transactionalRequestHandler);
                        bind(UpdateTransactionalRequestHandler.class).toInstance(RequestTests.updateTransactionalRequestHandler);
                        bind(FailForTransactionalRequestHandler.class).toInstance(RequestTests.failForTransactionalRequestHandler);
                    }
                })
        );
    }

    static void createSpies(Injector handlerInjector) {
        RequestTests.nullRequestHandler = Mockito.spy(handlerInjector.getInstance(NullRequestHandler.class));
        RequestTests.pingRequestHandler = Mockito.spy(handlerInjector.getInstance(PingRequestHandler.class));
        RequestTests.injectionRequestHandler = Mockito.spy(handlerInjector.getInstance(InjectionRequestHandler.class));
        RequestTests.transactionalRequestHandler = Mockito.spy(handlerInjector.getInstance(TransactionalRequestHandler.class));
        RequestTests.updateTransactionalRequestHandler = Mockito.spy(handlerInjector.getInstance(UpdateTransactionalRequestHandler.class));
        RequestTests.failForTransactionalRequestHandler = Mockito.spy(handlerInjector.getInstance(FailForTransactionalRequestHandler.class));
    }


}
