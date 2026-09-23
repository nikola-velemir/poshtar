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
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/notification/InjectedNotification.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/notification/InjectedNotificationHandler.java");
        static final JavaFileObject failingConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/notification/FailingNotificationConsumer.java");

        static final JavaFileObject validConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/notification/ValidNotificationConsumer.java");
        static final JavaFileObject[] validSet = {notification, handler, validConsumer};
        static final JavaFileObject[] failingSet = {notification, handler, failingConsumer};
    }

    private static class Query {
        static final JavaFileObject query =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/query/InjectedQuery.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/query/InjectedHandler.java");
        static final JavaFileObject failingConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/query/FailingConsumer.java");

        static final JavaFileObject validConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/query/ValidConsumer.java");
        static final JavaFileObject[] validSet = {query, handler, validConsumer};
        static final JavaFileObject[] failingSet = {query, handler, failingConsumer};
    }
    private static class VoidCommand {
        static final JavaFileObject command =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/voidCommand/InjectedVoidCommand.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/voidCommand/InjectedHandler.java");
        static final JavaFileObject failingConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/voidCommand/FailingConsumer.java");

        static final JavaFileObject validConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/voidCommand/ValidConsumer.java");
        static final JavaFileObject[] validSet = {command, handler, validConsumer};
        static final JavaFileObject[] failingSet = {command, handler, failingConsumer};
    }

    private static class Command {
        static final JavaFileObject command =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/command/InjectedCommand.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/command/InjectedHandler.java");
        static final JavaFileObject failingConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/command/FailingConsumer.java");

        static final JavaFileObject validConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/command/ValidConsumer.java");
        static final JavaFileObject[] validSet = {command, handler, validConsumer};
        static final JavaFileObject[] failingSet = {command, handler, failingConsumer};
    }

    private static class Request {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/request/InjectedRequest.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/request/InjectedRequestHandler.java");
        static final JavaFileObject failingConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/request/FailingRequestConsumer.java");

        static final JavaFileObject validConsumer =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/handler/request/ValidRequestConsumer.java");
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
    @DisplayName("Compilation fails when command components injected but not overruled!")
    void shouldFailCompilation_whenCommandComponentsNotOverruled() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Command.failingSet);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("PoshtaR VIOLATION: Handlers cannot be injected, set thru methods or constructor, or manually managed. Use 'Poshtar.send(request)'")
                .inFile(Command.failingConsumer);
    }

    @Test
    @DisplayName("Compilation passes when command components injected and overruled!")
    void shouldFailCompilation_whenCommandComponentsOverruled() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Command.validSet);

        assertThat(compilation).succeeded();
    }

    @Test
    @DisplayName("Compilation fails when void command components injected but not overruled!")
    void shouldFailCompilation_whenVoidCommandComponentsNotOverruled() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(VoidCommand.failingSet);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("PoshtaR VIOLATION: Handlers cannot be injected, set thru methods or constructor, or manually managed. Use 'Poshtar.send(request)'")
                .inFile(VoidCommand.failingConsumer);
    }

    @Test
    @DisplayName("Compilation passes when void command components injected and overruled!")
    void shouldFailCompilation_whenVoidCommandComponentsOverruled() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(VoidCommand.validSet);

        assertThat(compilation).succeeded();
    }
    @Test
    @DisplayName("Compilation fails when query components injected but not overruled!")
    void shouldFailCompilation_whenQueryComponentsNotOverruled() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Query.failingSet);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("PoshtaR VIOLATION: Handlers cannot be injected, set thru methods or constructor, or manually managed. Use 'Poshtar.send(request)'")
                .inFile(Query.failingConsumer);
    }

    @Test
    @DisplayName("Compilation passes when query components injected and overruled!")
    void shouldFailCompilation_whenQueryComponentsOverruled() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Query.validSet);

        assertThat(compilation).succeeded();
    }
    @Disabled("Request is no longer used.")
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
    @Disabled("Request is no longer used.")
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
