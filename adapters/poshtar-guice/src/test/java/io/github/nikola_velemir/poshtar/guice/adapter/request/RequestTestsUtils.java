package io.github.nikola_velemir.poshtar.guice.adapter.request;

import com.google.inject.*;
import com.google.inject.util.Modules;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.TestModule;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining.*;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.DummyLoggingService;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.InjectionRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.nullRequest.NullRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.ping.PingRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.fail.FailForTransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.TransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.UpdateTransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import org.mockito.Mockito;

import java.lang.reflect.Field;

@OverruleNoInjection
class RequestTestsUtils {
    static void createSpies(Injector handlerInjector) {
        // 1. Create all spies from the handler injector
        RequestTests.nullRequestHandler = Mockito.spy(handlerInjector.getInstance(NullRequestHandler.class));
        RequestTests.pingRequestHandler = Mockito.spy(handlerInjector.getInstance(PingRequestHandler.class));
        RequestTests.injectionRequestHandler = Mockito.spy(handlerInjector.getInstance(InjectionRequestHandler.class));
        RequestTests.transactionalRequestHandler = Mockito.spy(handlerInjector.getInstance(TransactionalRequestHandler.class));
        RequestTests.updateTransactionalRequestHandler = Mockito.spy(handlerInjector.getInstance(UpdateTransactionalRequestHandler.class));
        RequestTests.failForTransactionalRequestHandler = Mockito.spy(handlerInjector.getInstance(FailForTransactionalRequestHandler.class));
        RequestTests.chainingFirstRequestHandler = Mockito.spy(handlerInjector.getInstance(ChainingFirstRequestHandler.class));
        RequestTests.chainingSecondRequestHandler = Mockito.spy(handlerInjector.getInstance(ChainingSecondRequestHandler.class));
    }

    static Injector buildTestInjector() {
        var injector = Guice.createInjector(Modules.override(new TestModule()).with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(DummyLoggingService.class).toInstance(RequestTests.dummyLoggingService);
                bind(NullRequestHandler.class).toInstance(RequestTests.nullRequestHandler);
                bind(PingRequestHandler.class).toInstance(RequestTests.pingRequestHandler);
                bind(InjectionRequestHandler.class).toInstance(RequestTests.injectionRequestHandler);
                bind(TransactionalRequestHandler.class).toInstance(RequestTests.transactionalRequestHandler);
                bind(UpdateTransactionalRequestHandler.class).toInstance(RequestTests.updateTransactionalRequestHandler);
                bind(FailForTransactionalRequestHandler.class).toInstance(RequestTests.failForTransactionalRequestHandler);
                bind(ChainingFirstRequestHandler.class).toInstance(RequestTests.chainingFirstRequestHandler);
                bind(ChainingSecondRequestHandler.class).toInstance(RequestTests.chainingSecondRequestHandler);

            }

        }));
         rewirePoshtarProvider(injector, RequestTests.chainingFirstRequestHandler);

        return injector;
    }

    private static void rewirePoshtarProvider(Injector injector, Object handlerSpy) {
        try {
            Provider<Poshtar> correctProvider = injector.getProvider(Poshtar.class);

            // Walk past the Mockito-generated subclass to find the real class
            // that declares the field.
            var className = handlerSpy.getClass().getName();
            Class<?> clazz = handlerSpy.getClass();
            while (clazz != null && !clazz.equals(ChainingFirstRequestHandler.class)) {
                clazz = clazz.getSuperclass();
            }
            if (clazz == null) {
                throw new IllegalStateException(String.format("Could not locate %s in spy's class hierarchy", className));
            }

            Field providerField = clazz.getDeclaredField("poshtarProvider");
            providerField.setAccessible(true);
            providerField.set(handlerSpy, correctProvider);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to rewire poshtarProvider on ChainingFirstRequestHandler spy", e);
        }
    }

}
