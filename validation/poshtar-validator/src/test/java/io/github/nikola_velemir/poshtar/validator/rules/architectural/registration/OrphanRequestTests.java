package io.github.nikola_velemir.poshtar.validator.rules.architectural.registration;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import io.github.nikola_velemir.poshtar.validator.processor.PoshtarValidationProcessor;
import io.github.nikola_velemir.poshtar.validator.rules.PoshtarProcessorTestBed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class OrphanRequestTests extends PoshtarProcessorTestBed {

    @Test
    @DisplayName("Compilation fails when a query has no registered handler")
    void shouldFailCompilation_whenQueryIsOrphan() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Query.Fail.set);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("[PoshtaR] PoshtaR VIOLATION: No handler registered for request 'test.fixtures.rules.architectural.registration.orphan.query.fail.UnhandledQuery'")
                .inFile(Query.Fail.unhandledQuery)
                .onLineContaining("public record UnhandledQuery");
    }

    @Test
    @DisplayName("Compilation fails when a request has no registered handler")
    void shouldFailCompilation_whenRequestIsOrphan() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Request.Fail.set);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("[PoshtaR] PoshtaR VIOLATION: No handler registered for request 'fixtures.rules.architectural.registration.orphan.UnhandledRequest'")
                .inFile(Request.Fail.unhandledRequest)
                .onLineContaining("public record UnhandledRequest");
    }

    @Test
    @DisplayName("Compilation succeeds with no orphan queires")
    void shouldPassCompilation_whenQueryotOrphan() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Query.Success.Matched.set);

        assertThat(compilation).succeeded();
        assertThat(compilation)
                .hadWarningCount(0);

    }

    @Test
    @DisplayName("Compilation succeeds with no orphan request")
    void shouldPassCompilation_whenRequestNotOrphan() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Request.Success.Matched.set);

        assertThat(compilation).succeeded();

        assertThat(compilation)
                .hadWarningCount(0);

    }
    @Test
    @DisplayName("Compilation succeeds with suppressed orphan query")
    void shouldPassCompilation_whenQuerySuppressed() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Query.Success.Suppressed.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }
    @Test
    @DisplayName("Compilation succeeds with suppressed orphan request")
    void shouldPassCompilation_whenRequestSuppressed() {
        Compilation compilation =
                createCompiler()
                        .withProcessors(createProcessor())
                        .compile(Request.Success.Suppressed.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }

    private static class Query {
        static class Fail {
            static final JavaFileObject unhandledQuery =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/query/fail/UnhandledQuery.java");

            static final JavaFileObject matchedQuery =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/query/success/MatchedQuery.java");
            static final JavaFileObject matchedHandler =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/query/success/MatchedQueryHandler.java");
            static final JavaFileObject[] set = {unhandledQuery, matchedHandler, matchedQuery};

        }

        static class Success {
            static class Matched {

                static final JavaFileObject query =
                        JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/query/success/MatchedQuery.java");
                static final JavaFileObject handler =
                        JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/query/success/MatchedQueryHandler.java");
                static final JavaFileObject[] set = {handler, query};

            }

            static class Suppressed {
                static final JavaFileObject query =
                        JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/query/success/SuppressedOrphanQuery.java");

                static final JavaFileObject matchedRequest =
                        JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/request/success/MatchedRequest.java");
                static final JavaFileObject matchedHandler =
                        JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/request/success/MatchedHandler.java");
                static final JavaFileObject[] set = {query, matchedHandler, matchedRequest};

            }
        }
    }

    private static class Request {
        static class Fail {

            static final JavaFileObject unhandledRequest =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/request/fail/UnhandledRequest.java");

            static final JavaFileObject matchedRequest =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/request/success/MatchedRequest.java");
            static final JavaFileObject matchedHandler =
                    JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/request/success/MatchedHandler.java");
            static final JavaFileObject[] set = {unhandledRequest, matchedHandler, matchedRequest};
        }

        static class Success {
            static class Matched {

                static final JavaFileObject request =
                        JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/request/success/MatchedRequest.java");
                static final JavaFileObject handler =
                        JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/request/success/MatchedHandler.java");
                static final JavaFileObject[] set = {handler, request};

            }

            static class Suppressed {
                static final JavaFileObject request =
                        JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/request/success/SuppressedOrphanRequest.java");

                static final JavaFileObject matchedRequest =
                        JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/request/success/MatchedRequest.java");
                static final JavaFileObject matchedHandler =
                        JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/request/success/MatchedHandler.java");
                static final JavaFileObject[] set = {request, matchedHandler, matchedRequest};

            }
        }
    }


    private PoshtarValidationProcessor createProcessor() {
        var validator = new RuleValidatorProvider.Orphan();
        return new PoshtarValidationProcessor(validator);
    }
}
