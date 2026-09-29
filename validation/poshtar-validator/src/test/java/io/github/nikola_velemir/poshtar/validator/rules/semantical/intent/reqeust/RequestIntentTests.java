package io.github.nikola_velemir.poshtar.validator.rules.semantical.intent.reqeust;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import io.github.nikola_velemir.poshtar.core.request.Command;
import io.github.nikola_velemir.poshtar.core.request.Query;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.VoidCommand;
import io.github.nikola_velemir.poshtar.core.request.handler.CommandHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.QueryHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.VoidCommandHandler;
import io.github.nikola_velemir.poshtar.validator.processor.PoshtarValidationProcessor;
import io.github.nikola_velemir.poshtar.validator.rules.PoshtarProcessorTestBed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class RequestIntentTests extends PoshtarProcessorTestBed {
    private static final String WARNING_MESSAGE = String.format("Using %s is discouraged, to better declare semantical intent use either %s, %s or %s",
            Request.class.getName(), Query.class.getName(), Command.class.getName(), VoidCommand.class.getName());
    @Test
    @DisplayName("Compilation warns when implementing request")
    void shouldWarn_whenImplementingRequest() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Fail.set);

        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .hadWarningCount(1);
        assertThat(compilation)
                .hadWarningContaining(WARNING_MESSAGE);
    }
    @Test
    @DisplayName("Compilation should not warn when implementing Query ")
    void shouldNotWarn_whenImplementingQuery() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Success.Query.set);

        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .hadWarningCount(0);
        assertThat(compilation)
                .succeededWithoutWarnings();
    }
    @Test
    @DisplayName("Compilation should not warn when implementing command")
    void shouldNotWarn_whenImplementingCommand() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Success.Command.set);

        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .hadWarningCount(0);
        assertThat(compilation)
                .succeededWithoutWarnings();

    }

    private PoshtarValidationProcessor createProcessor() {
        var validator = new RuleValidatorProvider.HandlerIntent();
        return new PoshtarValidationProcessor(validator);
    }

    private static class Success {
        static class Command {
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/request/success/command/SucceedsCommand.java");
            static final JavaFileObject handler =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/request/success/command/SucceedsCommandHandler.java");
            static final JavaFileObject[] set = {request, handler};
        }
        static class Query {
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/request/success/query/SucceedsQuery.java");
            static final JavaFileObject handler =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/request/success/query/SucceedsQueryHandler.java");
            static final JavaFileObject[] set = {request, handler};
        }
    }

    private static class Fail {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/request/fail/FlagsRequest.java");
        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/request/fail/FlagsHandler.java");
        static final JavaFileObject[] set = {request, handler};

    }
}
