package io.github.nikola_velemir.poshtar.validator.rules.architectural.deadPipeline;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import io.github.nikola_velemir.poshtar.validator.processor.PoshtarValidationProcessor;
import io.github.nikola_velemir.poshtar.validator.rules.PoshtarProcessorTestBed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class DeadPipelineTests extends PoshtarProcessorTestBed {

    private static class Dead {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/dead/DeadPipelineRequest.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/dead/DeadPipelineRequestHandler.java");
        static final JavaFileObject behaviour =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/dead/DeadPipelineBehaviour.java");

        static final JavaFileObject[] set = {request, handler, behaviour};
    }

    private static class Throwing {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/throw/ThrowingRequest.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/throw/ThrowingHandler.java");
        static final JavaFileObject basicBehaviour =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/throw/BasicThrowingBehaviour.java");
        static final JavaFileObject doubleBehaviour =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/throw/DoubleThrowingBehaviour.java");
        static final JavaFileObject[] set = {request, handler, basicBehaviour, doubleBehaviour};
    }

    private static class Alive {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/alive/AlivePipelineRequest.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/alive/AlivePipelineHandler.java");
        static final JavaFileObject behaviour =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/alive/AlivePipelineBehaviour.java");

        static final JavaFileObject[] set = {request, handler, behaviour};
    }

    private static class Suppressed {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/suppressed/SuppressedRequest.java");
        static final JavaFileObject behaviour =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/suppressed/SuppressedBehaviour.java");
        static final JavaFileObject[] set = {request, behaviour};

    }

    private static class Shadow {
        static class Dead {
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/shadow/dead/ShadowDeadRequest.java");
            static final JavaFileObject handler =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/shadow/dead/ShadowDeadHandler.java");
            static final JavaFileObject behaviour =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/shadow/dead/ShadowDeadBehaviour.java");

            static final JavaFileObject[] set = {request, handler, behaviour};
        }

        static class Alive {
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/shadow/alive/ShadowAliveRequest.java");
            static final JavaFileObject handler =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/shadow/alive/ShadowAliveHandler.java");
            static final JavaFileObject behaviour =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/shadow/alive/ShadowAliveBehaviour.java");

            static final JavaFileObject[] set = {request, handler, behaviour};
        }
    }

    @Test
    @DisplayName("Compilation warns when pipeline dead")
    void shouldWarn_whenPipelineDead() {
        Compilation compilation = createCompiler()
                .withProcessors(
                       createProcessor()
                ).compile(Dead.set);

        assertThat(compilation).succeeded();

        assertThat(compilation)
                .hadWarningContaining("[PoshtaR] PoshtaR VIOLATION: Behaviour must either call 'next.handle(request)' or throw an exception")
                .inFile(Dead.behaviour);

    }

    @Test
    @DisplayName("Compilation does not warn when pipeline alive")
    void shouldNotWarn_whenPipelineAlive() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Alive.set);


        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }

    @Test
    @DisplayName("Compilation does not warn when behaviours throw")
    void shouldNotWarn_whenBehavioursThrow() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Throwing.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }

    @Test
    @DisplayName("Compilation warns when behaviours shadows")
    void shouldWarn_whenShadows() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Shadow.Dead.set);

        assertThat(compilation).succeeded();
        assertThat(compilation)
                .hadWarningContaining("[PoshtaR] PoshtaR VIOLATION: Behaviour must either call 'next.handle(request)' or throw an exception.")
                .inFile(Shadow.Dead.behaviour);
    }

    @Test
    @DisplayName("Compilation does not warn when behaviours shadows")
    void shouldNotWarn_whenShadows() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Shadow.Alive.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }
    @Test
    @DisplayName("Compilation does not warn when suppressed")
    void shouldNotWarn_whenSuppressed() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Suppressed.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }
    private PoshtarValidationProcessor createProcessor(){
        var ruleProvider = new RuleValidator();
        return new PoshtarValidationProcessor(ruleProvider);
    }
}
