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
    @DisplayName("Compilation fails for wiring handler with behaviour")
    void shouldFail_whenWiringHandlerWithBehaviour() {
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
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/fail/requestHandler/withBehaviour/FailWithBehaviourRequest.java");
                static final JavaFileObject handler =
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/wiring/handler/fail/requestHandler/withBehaviour/FailWithBehaviourHandler.java");
                static final JavaFileObject[] set = {request, handler};
            }
        }
    }

    private PoshtarValidationProcessor createProcessor() {
        var validator = new RuleValidatorProvider();
        return new PoshtarValidationProcessor(validator);
    }
}
