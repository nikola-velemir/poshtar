package io.github.nikola_velemir.poshtar.validator.rules.semantical.wiring.behaviour;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import io.github.nikola_velemir.poshtar.validator.processor.PoshtarValidationProcessor;
import io.github.nikola_velemir.poshtar.validator.rules.PoshtarProcessorTestBed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class BehaviourWiringTests extends PoshtarProcessorTestBed {

    @Test
    @DisplayName("Compilation fails for wiring request handler")
    void shouldFail_whenWiringRequestHandler() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Fail.RequestHandler.set);

        assertThat(compilation)
                .failed();

        assertThat(compilation)
                .hadErrorCount(1);

        assertThat(compilation)
                .hadErrorContaining("[PoshtaR] Class annotated with @Behaviour must implement PipelineBehaviour.")
                .inFile(Fail.RequestHandler.behaviour);
    }

    @Test
    @DisplayName("Compilation fails for wiring command handler")
    void shouldFail_whenWiringCommandHandler() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Fail.CommandHandler.set);

        assertThat(compilation)
                .failed();

        assertThat(compilation)
                .hadErrorCount(1);

        assertThat(compilation)
                .hadErrorContaining("[PoshtaR] Class annotated with @Behaviour must implement PipelineBehaviour.")
                .inFile(Fail.CommandHandler.behaviour);
    }

    @Test
    @DisplayName("Compilation fails for wiring void command handler")
    void shouldFail_whenWiringVoidCommandHandler() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Fail.VoidCommandHandler.set);

        assertThat(compilation)
                .failed();

        assertThat(compilation)
                .hadErrorCount(1);

        assertThat(compilation)
                .hadErrorContaining("[PoshtaR] Class annotated with @Behaviour must implement PipelineBehaviour.")
                .inFile(Fail.VoidCommandHandler.behaviour);
    }

    @Test
    @DisplayName("Compilation fails for wiring query handler")
    void shouldFail_whenWiringQueryHandler() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Fail.QueryHandler.set);

        assertThat(compilation)
                .failed();

        assertThat(compilation)
                .hadErrorCount(1);

        assertThat(compilation)
                .hadErrorContaining("[PoshtaR] Class annotated with @Behaviour must implement PipelineBehaviour.")
                .inFile(Fail.QueryHandler.behaviour);
    }

    @Test
    @DisplayName("Compilation fails for wiring notification handler")
    void shouldFail_whenWiringNotificationHandler() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Fail.NotificationHandler.set);

        assertThat(compilation)
                .failed();

        assertThat(compilation)
                .hadErrorCount(1);

        assertThat(compilation)
                .hadErrorContaining("[PoshtaR] Class annotated with @Behaviour must implement PipelineBehaviour.")
                .inFile(Fail.NotificationHandler.behaviour);
    }

    @Test
    @DisplayName("Compilation succeeds for correct wiring")
    void shouldSucceed_whenWiredCorrectly() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Success.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }

    private static class Success {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/valid/ValidRequest.java");
        static final JavaFileObject behaviour =
                JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/valid/ValidRequestBehaviour.java");
        static final JavaFileObject query =
                JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/valid/ValidQuery.java");
        static final JavaFileObject queryBehaviour =
                JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/valid/ValidQueryBehaviour.java");
        static final JavaFileObject command =
                JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/valid/ValidCommand.java");
        static final JavaFileObject commandBehaviour =
                JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/valid/ValidCommandBehaviour.java");
        static final JavaFileObject voidCommand =
                JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/valid/ValidVoidCommand.java");
        static final JavaFileObject voidCommandBehaviour =
                JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/valid/ValidVoidCommandBehaviour.java");

        static final JavaFileObject[] set = {request, behaviour, query, queryBehaviour, command, commandBehaviour, voidCommand, voidCommandBehaviour};

    }

    private static class Fail {
        static class VoidCommandHandler {
            static final JavaFileObject command =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/fail/voidCommandHandler/FailVoidCommand.java");
            static final JavaFileObject behaviour =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/fail/voidCommandHandler/FailVoidHandler.java");
            static final JavaFileObject[] set = {command, behaviour};
        }

        static class QueryHandler {
            static final JavaFileObject query =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/fail/queryHandler/FailQuery.java");
            static final JavaFileObject behaviour =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/fail/queryHandler/FailQueryHandler.java");
            static final JavaFileObject[] set = {query, behaviour};
        }

        static class CommandHandler {
            static final JavaFileObject command =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/fail/commandHandler/FailCommand.java");
            static final JavaFileObject behaviour =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/fail/commandHandler/FailBehaviour.java");
            static final JavaFileObject[] set = {command, behaviour};
        }

        static class RequestHandler {
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/fail/requestHandler/RequestHandlerRequest.java");
            static final JavaFileObject behaviour =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/fail/requestHandler/RequestHandlerBehaviour.java");
            static final JavaFileObject[] set = {request, behaviour};
        }

        static class NotificationHandler {
            static final JavaFileObject notification =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/fail/notificationHandler/NotificationHandlerNotification.java");
            static final JavaFileObject behaviour =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/fail/notificationHandler/NotificationHandlerBehaviour.java");
            static final JavaFileObject[] set = {notification, behaviour};
        }
    }

    private PoshtarValidationProcessor createProcessor() {
        var validator = new RuleValidatorProvider();
        return new PoshtarValidationProcessor(validator);
    }
}
