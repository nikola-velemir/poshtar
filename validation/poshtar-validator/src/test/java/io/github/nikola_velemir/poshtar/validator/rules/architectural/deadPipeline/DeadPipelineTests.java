package io.github.nikola_velemir.poshtar.validator.rules.architectural.deadPipeline;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static io.github.nikola_velemir.poshtar.validator.rules.TestUtils.compile;

public class DeadPipelineTests {

    private static class Dead {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/DeadPipelineRequest.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/DeadPipelineRequestHandler.java");
        static final JavaFileObject behaviour =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/DeadPipelineBehaviour.java");

        static final JavaFileObject[] set = {request, handler, behaviour};
    }

    private static class Throwing {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/ThrowingRequest.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/ThrowingHandler.java");
        static final JavaFileObject basicBehaviour =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/BasicThrowingBehaviour.java");
        static final JavaFileObject doubleBehaviour =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/DoubleThrowingBehaviour.java");
        static final JavaFileObject[] set = {request, handler, basicBehaviour, doubleBehaviour};
    }

    private static class Alive {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/AlivePipelineRequest.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/AlivePipelineHandler.java");
        static final JavaFileObject behaviour =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/deadPipeline/AlivePipelineBehaviour.java");

        static final JavaFileObject[] set = {request, handler, behaviour};
    }


    @Test
    @DisplayName("Compilation warns when pipeline dead")
    void shouldWarn_whenPipelineDead() {
        Compilation compilation = compile(Dead.set);

        assertThat(compilation).succeeded();

        assertThat(compilation)
                .hadWarningContaining("[PoshtaR] PoshtaR VIOLATION: Behaviour must either call 'next.handle(request)' or throw an exception")
                .inFile(Dead.behaviour);

    }

    @Test
    @DisplayName("Compilation does not warn when pipeline alive")
    void shouldNotWarn_whenPipelineAlive() {
        Compilation compilation = compile(Alive.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }
    @Test
    @DisplayName("Compilation does not warn when behaviours throw")
    void shouldNotWarn_whenBehavioursThrow() {
        Compilation compilation = compile(Throwing.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }
}
