package io.github.nikola_velemir.poshtar.validator.rules.semantical.responsiblity;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import io.github.nikola_velemir.poshtar.validator.processor.PoshtarValidationProcessor;
import io.github.nikola_velemir.poshtar.validator.rules.PoshtarProcessorTestBed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class SingleResponsibilityTests extends PoshtarProcessorTestBed {
    @Test
    @DisplayName("Compilation succeeds when exactly one interface is implemented")
    void shouldSucceed_whenExactlyOnInterfaceImplemented() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Single.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }

    @Test
    @DisplayName("Compilation fails when all implemented")
    void shouldFail_whenAllImplemented() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(All.Basic.set);

        assertThat(compilation).failed();

        assertThat(compilation).hadErrorContaining("[PoshtaR] PoshtaR VIOLATION: A class implementing io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler or io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler or io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour may only implement one of given interfaces.");
    }


    private static class All {
        static class Basic {
            static final JavaFileObject request = JavaFileObjects.forResource("test/fixtures/rules/semantical/responsiblity/all/WhenAllRequest.java");
            static final JavaFileObject godClass = JavaFileObjects.forResource("test/fixtures/rules/semantical/responsiblity/all/WhenAll.java");

            static final JavaFileObject notification = JavaFileObjects.forResource("test/fixtures/rules/semantical/responsiblity/all/WhenAllNotification.java");
            static final JavaFileObject[] set = {request, godClass, notification};

        }
        static class AllRequests{

        }

    }

    private static class Single {
        static final JavaFileObject request = JavaFileObjects.forResource("test/fixtures/rules/semantical/responsiblity/exactlyOne/SingleRequest.java");
        static final JavaFileObject behaviour = JavaFileObjects.forResource("test/fixtures/rules/semantical/responsiblity/exactlyOne/SingleBehaviour.java");
        static final JavaFileObject requestHandler = JavaFileObjects.forResource("test/fixtures/rules/semantical/responsiblity/exactlyOne/SingleRequestHandler.java");

        static final JavaFileObject notification = JavaFileObjects.forResource("test/fixtures/rules/semantical/responsiblity/exactlyOne/SingleNotification.java");

        static final JavaFileObject notificationHandler = JavaFileObjects.forResource("test/fixtures/rules/semantical/responsiblity/exactlyOne/SingleNotificationHandler.java");

        static final JavaFileObject[] set = {request, behaviour, requestHandler, notification, notificationHandler};

    }

    private PoshtarValidationProcessor createProcessor() {
        var validator = new RuleValidatorProvider.SingleResponsibility();
        return new PoshtarValidationProcessor(validator);
    }
}
