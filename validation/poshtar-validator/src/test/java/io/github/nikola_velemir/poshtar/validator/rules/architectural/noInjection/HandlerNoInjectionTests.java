package io.github.nikola_velemir.poshtar.validator.rules.architectural.noInjection;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import io.github.nikola_velemir.poshtar.validator.processor.PoshtarValidationProcessor;
import io.github.nikola_velemir.poshtar.validator.rules.PoshtarProcessorTestBed;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class HandlerNoInjectionTests extends PoshtarProcessorTestBed {
    private static class Notification {
        static final JavaFileObject notification =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/InjectedNotification.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/InjectedNotificationHandler.java");
        static final JavaFileObject failingConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/FailingNotificationConsumer.java");

        static final JavaFileObject validConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/ValidNotificationConsumer.java");
        static final JavaFileObject[] validSet = {notification, handler, validConsumer};
        static final JavaFileObject[] failingSet = {notification, handler, failingConsumer};
    }

    private static class Request {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/InjectedRequest.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/InjectedRequestHandler.java");
        static final JavaFileObject failingConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/FailingRequestConsumer.java");

        static final JavaFileObject validConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/ValidRequestConsumer.java");
        static final JavaFileObject[] validSet = {request, handler, validConsumer};
        static final JavaFileObject[] failingSet = {request, handler, failingConsumer};
    }

    @Test
    @DisplayName("Compilation fails when notification components injected but not overruled!")
    void shouldFailCompilation_whenNotificationComponentsNotOverruled() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Notification.failingSet);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("PoshtaR VIOLATION: Handlers cannot be injected, set thru methods or constructor, or manually managed. Use 'Poshtar.send(request)'")
                .inFile(Notification.failingConsumer);
    }

    @Test
    @DisplayName("Compilation passes when notification components injected and overruled!")
    void shouldFailCompilation_whenNotificationComponentsOverruled() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Notification.validSet);

        assertThat(compilation).succeeded();
    }

    @Test
    @DisplayName("Compilation fails when request components injected but not overruled!")
    void shouldFailCompilation_whenRequestComponentsNotOverruled() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Request.failingSet);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("PoshtaR VIOLATION: Handlers cannot be injected, set thru methods or constructor, or manually managed. Use 'Poshtar.send(request)'")
                .inFile(Request.failingConsumer);
    }

    @Test
    @DisplayName("Compilation passes when request components injected and overruled!")
    void shouldFailCompilation_whenRequestComponentsOverruled() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Request.validSet);

        assertThat(compilation).succeeded();
    }

    private PoshtarValidationProcessor createProcessor() {
        var validator = new RuleValidatorProvider.Handler();
        return new PoshtarValidationProcessor(validator);
    }
}
