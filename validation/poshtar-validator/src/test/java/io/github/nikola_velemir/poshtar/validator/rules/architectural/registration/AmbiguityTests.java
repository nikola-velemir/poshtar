package io.github.nikola_velemir.poshtar.validator.rules.architectural.registration;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import io.github.nikola_velemir.poshtar.validator.processor.PoshtarValidationProcessor;
import io.github.nikola_velemir.poshtar.validator.rules.PoshtarProcessorTestBed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class AmbiguityTests extends PoshtarProcessorTestBed {

    private final JavaFileObject request =
            JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/AmbiguousRequest.java");
    private final JavaFileObject handlerOne =
            JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/AmbiguityRequestFirstHandler.java");
    private final JavaFileObject handlerTwo =
            JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/AmbiguityRequestSecondHandler.java");

    // Pre-bundled scenarios
    private final JavaFileObject[] ambiguousSet = {request, handlerOne, handlerTwo};
    private final JavaFileObject[] validSingleHandlerSet = {request, handlerOne};

    @Test
    @DisplayName("Compilation fails when a request has multiple handlers registered")
    void shouldFailCompilation_whenDuplicateHandlersExist() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(ambiguousSet);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("Ambiguity! Request")
                .inFile(handlerOne);

        assertThat(compilation)
                .hadErrorContaining("Ambiguity! Request")
                .inFile(handlerTwo);

        assertThat(compilation)
                .hadErrorContaining("AmbiguityRequestFirstHandler")
                .inFile(handlerTwo);
        assertThat(compilation)
                .hadErrorContaining("AmbiguityRequestSecondHandler")
                .inFile(handlerOne);
    }

    @Test
    @DisplayName("Compilation succeeds when exactly one handler is registered")
    void shouldSucceed_whenSingleHandlerIsRegistered() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(validSingleHandlerSet);

        assertThat(compilation).succeeded();
    }
    private PoshtarValidationProcessor createProcessor() {
        var validator = new RuleValidatorProvider.Ambiguity();
        return new PoshtarValidationProcessor(validator);
    }

}
