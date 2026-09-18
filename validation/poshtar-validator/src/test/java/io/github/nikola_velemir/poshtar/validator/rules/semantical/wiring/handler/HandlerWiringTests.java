package io.github.nikola_velemir.poshtar.validator.rules.semantical.wiring.handler;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import io.github.nikola_velemir.poshtar.validator.processor.PoshtarValidationProcessor;
import io.github.nikola_velemir.poshtar.validator.rules.PoshtarProcessorTestBed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class HandlerWiringTests extends PoshtarProcessorTestBed {

    @Test
    @DisplayName("Compilation succeeds for correct request handler wiring")
    void shouldSucceed_whenRequestHandlerWiredCorrectly() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Success.RequestHandler.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }

    @Test
    @DisplayName("Compilation succeeds for correct notification handler wiring")
    void shouldSucceed_whenNotificationHandlerWiredCorrectly() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Success.NotificationHandler.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }
    @Test
    @DisplayName("Compilation fails for wiring void command handler with behaviour")
    void shouldFail_whenWiringVoidCommandHandlerWithBehaviour() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Fail.VoidCommandHandler.Behaviour.set);

        assertThat(compilation)
                .failed();

        assertThat(compilation)
                .hadErrorCount(1);

        assertThat(compilation)
                .hadErrorContaining("[PoshtaR] Class annotated with @Handler must implement RequestHandler or NotificationHandler.")
                .inFile(Fail.VoidCommandHandler.Behaviour.handler);
    }
    @Test
    @DisplayName("Compilation fails for wiring command handler with behaviour")
    void shouldFail_whenWiringCommandHandlerWithBehaviour() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Fail.CommandHandler.Behaviour.set);

        assertThat(compilation)
                .failed();

        assertThat(compilation)
                .hadErrorCount(1);

        assertThat(compilation)
                .hadErrorContaining("[PoshtaR] Class annotated with @Handler must implement RequestHandler or NotificationHandler.")
                .inFile(Fail.CommandHandler.Behaviour.handler);
    }

    @Test
    @DisplayName("Compilation fails for wiring query handler with behaviour")
    void shouldFail_whenWiringQueryHandlerWithBehaviour() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Fail.QueryHandler.Behaviour.set);

        assertThat(compilation)
                .failed();

        assertThat(compilation)
                .hadErrorCount(1);

        assertThat(compilation)
                .hadErrorContaining("[PoshtaR] Class annotated with @Handler must implement RequestHandler or NotificationHandler.")
                .inFile(Fail.QueryHandler.Behaviour.handler);
    }

    @Test
    @DisplayName("Compilation fails for wiring request handler with behaviour")
    void shouldFail_whenWiringRequestHandlerWithBehaviour() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Fail.RequestHandler.Behaviour.set);

        assertThat(compilation)
                .failed();

        assertThat(compilation)
                .hadErrorCount(1);

        assertThat(compilation)
                .hadErrorContaining("[PoshtaR] Class annotated with @Handler must implement RequestHandler or NotificationHandler.")
                .inFile(Fail.RequestHandler.Behaviour.handler);
    }

    private static class Success {
        static class RequestHandler {
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/success/requestHandler/ValidRequest.java");
            static final JavaFileObject handler =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/success/requestHandler/ValidHandler.java");
            static final JavaFileObject[] set = {request, handler};
        }

        static class NotificationHandler {
            static final JavaFileObject notification =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/success/notificationHandler/ValidNotification.java");
            static final JavaFileObject handler =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/success/notificationHandler/ValidHandler.java");
            static final JavaFileObject[] set = {notification, handler};
        }

    }

    private static class Fail {
        static class RequestHandler {
            static class Behaviour {
                static final JavaFileObject request =
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/fail/request/FailWithBehaviourRequest.java");
                static final JavaFileObject handler =
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/fail/request/FailWithBehaviourHandler.java");
                static final JavaFileObject[] set = {request, handler};
            }
        }

        static class QueryHandler {
            static class Behaviour {
                static final JavaFileObject query =
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/fail/query/FailWithBehaviourQuery.java");
                static final JavaFileObject handler =
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/fail/query/FailWithBehaviourHandler.java");
                static final JavaFileObject[] set = {query, handler};
            }
        }

        static class CommandHandler {
            static class Behaviour {
                static final JavaFileObject command =
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/fail/command/FailWithBehaviourCommand.java");
                static final JavaFileObject handler =
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/fail/command/FailWithBehaviourHandler.java");
                static final JavaFileObject[] set = {command, handler};
            }
        }

        static class VoidCommandHandler {
            static class Behaviour {
                static final JavaFileObject command =
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/fail/voidCommand/FailWithBehaviourVoidCommand.java");
                static final JavaFileObject handler =
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/fail/voidCommand/FailWithBehaviourVoidHandler.java");
                static final JavaFileObject[] set = {command, handler};
            }
        }
    }

    private PoshtarValidationProcessor createProcessor() {
        var validator = new RuleValidatorProvider();
        return new PoshtarValidationProcessor(validator);
    }
}
