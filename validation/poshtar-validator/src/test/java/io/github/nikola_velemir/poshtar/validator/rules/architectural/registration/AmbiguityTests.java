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
    private static class Success{
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/request/AmbiguousRequest.java");
        static final JavaFileObject handlerOne =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/request/AmbiguityRequestFirstHandler.java");
        static final JavaFileObject[] set = {request, handlerOne};

    }
    private static class Fail{
        static class Request{
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/request/AmbiguousRequest.java");
            static final JavaFileObject handlerOne =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/request/AmbiguityRequestFirstHandler.java");
            static final JavaFileObject handlerTwo =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/request/AmbiguityRequestSecondHandler.java");
            static final JavaFileObject[] set = {request, handlerOne, handlerTwo};

        }
        static class VoidCommand{
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/voidCommand/AmbiguousRequest.java");
            static final JavaFileObject handlerOne =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/voidCommand/AmbiguityRequestFirstHandler.java");
            static final JavaFileObject handlerTwo =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/voidCommand/AmbiguityRequestSecondHandler.java");
            static final JavaFileObject[] set = {request, handlerOne, handlerTwo};

        }
        static class Command{
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/command/AmbiguousRequest.java");
            static final JavaFileObject handlerOne =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/command/AmbiguityRequestFirstHandler.java");
            static final JavaFileObject handlerTwo =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/command/AmbiguityRequestSecondHandler.java");
            static final JavaFileObject[] set = {request, handlerOne, handlerTwo};

        }
        static class Query{
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/query/AmbiguousQuery.java");
            static final JavaFileObject handlerOne =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/query/QueryHandler.java");
            static final JavaFileObject handlerTwo =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/ambiguity/query/QueryHandlerAlt.java");
            static final JavaFileObject[] set = {request, handlerOne, handlerTwo};

        }
    }

    // Pre-bundled scenarios

    @Test
    @DisplayName("Compilation fails when a void command has multiple void command handlers registered")
    void shouldFailCompilation_whenDuplicateVoidCommandHandlersExist() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Fail.VoidCommand.set);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("Ambiguity! Request")
                .inFile(Fail.VoidCommand.handlerOne);

        assertThat(compilation)
                .hadErrorContaining("Ambiguity! Request")
                .inFile(Fail.VoidCommand.handlerTwo);
    }
    @Test
    @DisplayName("Compilation fails when a command has multiple command handlers registered")
    void shouldFailCompilation_whenDuplicateCommandHandlersExist() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Fail.Command.set);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("Ambiguity! Request")
                .inFile(Fail.Command.handlerOne);

        assertThat(compilation)
                .hadErrorContaining("Ambiguity! Request")
                .inFile(Fail.Command.handlerTwo);
    }
    @Test
    @DisplayName("Compilation fails when a query has multiple query handlers registered")
    void shouldFailCompilation_whenDuplicateQueryHandlersExist() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Fail.Query.set);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("Ambiguity! Request")
                .inFile(Fail.Query.handlerOne);

        assertThat(compilation)
                .hadErrorContaining("Ambiguity! Request")
                .inFile(Fail.Query.handlerTwo);
    }
    @Test
    @DisplayName("Compilation fails when a request has multiple handlers registered")
    void shouldFailCompilation_whenDuplicateHandlersExist() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Fail.Request.set);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("Ambiguity! Request")
                .inFile(Fail.Request.handlerOne);

        assertThat(compilation)
                .hadErrorContaining("Ambiguity! Request")
                .inFile(Fail.Request.handlerTwo);

        assertThat(compilation)
                .hadErrorContaining("AmbiguityRequestFirstHandler")
                .inFile(Fail.Request.handlerTwo);
        assertThat(compilation)
                .hadErrorContaining("AmbiguityRequestSecondHandler")
                .inFile(Fail.Request.handlerOne);
    }

    @Test
    @DisplayName("Compilation succeeds when exactly one handler is registered")
    void shouldSucceed_whenSingleHandlerIsRegistered() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Success.set);

        assertThat(compilation).succeeded();
    }
    private PoshtarValidationProcessor createProcessor() {
        var validator = new RuleValidatorProvider.Ambiguity();
        return new PoshtarValidationProcessor(validator);
    }

}
