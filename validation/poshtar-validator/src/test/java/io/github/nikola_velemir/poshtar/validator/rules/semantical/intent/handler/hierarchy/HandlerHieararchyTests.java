package io.github.nikola_velemir.poshtar.validator.rules.semantical.intent.handler.hierarchy;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import io.github.nikola_velemir.poshtar.core.request.handler.CommandHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.QueryHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.validator.processor.PoshtarValidationProcessor;
import io.github.nikola_velemir.poshtar.validator.rules.PoshtarProcessorTestBed;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class HandlerHieararchyTests extends PoshtarProcessorTestBed {
    private static final String VIOLATION_MESSAGE = String.format(
            "must not extend or implement %s directly. Use %s or %s.",
            RequestHandler.class.getName(),
            QueryHandler.class.getName(),
            CommandHandler.class.getName()
    );

    @Test
    @DisplayName("Compilation warns when implementing request handler for query")
    void shouldWarn_whenImplementingRequestHandlerForQuery() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Fail.Query.set);

        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .hadWarningCount(1);
        assertThat(compilation)
                .hadWarningContaining(VIOLATION_MESSAGE);
    }

    @Test
    @DisplayName("Compilation warns when implementing request handler for void command")
    void shouldWarn_whenImplementingRequestHandlerForVoidCommand() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Fail.VoidCommand.Basic.set);

        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .hadWarningCount(1);
        assertThat(compilation)
                .hadWarningContaining(VIOLATION_MESSAGE);
    }
    @Disabled("Should work, request handler hierarchy is more crucial than this, because at base this is request to request handler separation")
    @Test
    @DisplayName("Compilation warns when implementing command handler for void command")
    void shouldWarn_whenImplementingCommandHandlerForVoidCommand() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Fail.VoidCommand.Command.set);

        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .hadWarningCount(1);
        assertThat(compilation)
                .hadWarningContaining(VIOLATION_MESSAGE);
    }

    @Test
    @DisplayName("Compilation warns when implementing request handler for command")
    void shouldWarn_whenImplementingRequestHandlerForCommand() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Fail.Command.set);

        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .hadWarningCount(1);
        assertThat(compilation)
                .hadWarningContaining(VIOLATION_MESSAGE);
    }

    @Test
    @DisplayName("Compilation warns when implementing command handler for command")
    void shouldNotWarn_whenImplementingCommandHandlerForCommand() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Success.Command.set);

        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .succeededWithoutWarnings();
    }

    @Test
    @DisplayName("Compilation should not warn when implementing query handler for query")
    void shouldNotWarn_whenImplementingQueryHandlerForQuery() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Success.Query.set);

        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .succeededWithoutWarnings();
    }

    @Test
    @DisplayName("Compilation should not warn when implementing void command handler for void command")
    void shouldNotWarn_whenImplementingVoidCommandHandlerForVoidCommand() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Success.VoidCommand.set);

        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .succeededWithoutWarnings();
    }

    private PoshtarValidationProcessor createProcessor() {
        var validator = new RuleValidatorProvider.Hierarchy();
        return new PoshtarValidationProcessor(validator);
    }

    private static class Success {
        static class Command {
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/success/command/SucceedsCommand.java");
            static final JavaFileObject handler =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/success/command/SucceedsHandler.java");
            static final JavaFileObject[] set = {request, handler};
        }

        static class VoidCommand {
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/success/voidCommand/SucceedVoidCommand.java");
            static final JavaFileObject handler =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/success/voidCommand/SucceedHandler.java");
            static final JavaFileObject[] set = {request, handler};
        }

        static class Query {
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/success/query/SucceedsQuery.java");
            static final JavaFileObject handler =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/success/query/SucceedsHandler.java");
            static final JavaFileObject[] set = {request, handler};
        }
    }

    private static class Fail {
        static class VoidCommand {
            static class Basic {
                static final JavaFileObject request =
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/fail/voidCommand/basic/FlagsVoidCommand.java");
                static final JavaFileObject handler =
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/fail/voidCommand/basic/FlagsHandler.java");
                static final JavaFileObject[] set = {request, handler};
            }

            static class Command {
                static final JavaFileObject request =
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/fail/voidCommand/command/FlagsCommand.java");
                static final JavaFileObject handler =
                        JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/fail/voidCommand/command/FlagsHandler.java");
                static final JavaFileObject[] set = {request, handler};

            }
        }


        static class Command {
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/fail/command/FlagsCommand.java");
            static final JavaFileObject handler =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/fail/command/FlagsHandler.java");
            static final JavaFileObject[] set = {request, handler};
        }

        static class Query {
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/fail/query/FlagsQuery.java");
            static final JavaFileObject handler =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/intent/handler/hierarchy/fail/query/FlagsHandler.java");
            static final JavaFileObject[] set = {request, handler};
        }
    }
}
