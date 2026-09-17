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
    private static class Success{
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/valid/ValidRequest.java");
        static final JavaFileObject behaviour =
                JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/valid/ValidBehaviour.java");
        static final JavaFileObject[] set = {request, behaviour};

    }
    private static class Fail{
        static class RequestHandler{
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/fail/requestHandler/RequestHandlerRequest.java");
            static final JavaFileObject behaviour =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/behaviour/fail/requestHandler/RequestHandlerBehaviour.java");
            static final JavaFileObject[] set = {request, behaviour};
        }
        static class NotificationHandler{
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
