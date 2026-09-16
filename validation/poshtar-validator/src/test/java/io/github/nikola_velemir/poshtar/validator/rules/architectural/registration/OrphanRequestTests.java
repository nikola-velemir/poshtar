package io.github.nikola_velemir.poshtar.validator.rules.architectural.registration;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static io.github.nikola_velemir.poshtar.validator.rules.TestUtils.compile;

public class OrphanRequestTests {


    @Test
    @DisplayName("Compilation fails when a request has no registered handler")
    void shouldFailCompilation_whenRequestIsOrphan() {
        Compilation compilation = compile(completeSet);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("[PoshtaR] PoshtaR VIOLATION: No handler registered for request 'fixtures.rules.architectural.registration.orphan.UnhandledRequest'")
                .inFile(unhandledRequest)
                .onLineContaining("public record UnhandledRequest");
    }

    @Test
    @DisplayName("Compilation succeeds with no orphan requestsr")
    void shouldPassCompilation_whenRequestNotOrphan() {
        Compilation compilation = compile(matched);

        assertThat(compilation).succeeded();

    }

    private final JavaFileObject unhandledRequest =
            JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/UnhandledRequest.java");
    private final JavaFileObject matchedRequest =
            JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/MatchedRequest.java");
    private final JavaFileObject matchedHandler =
            JavaFileObjects.forResource("test/fixtures/rules/architectural/registration/orphan/MatchedHandler.java");
    private final JavaFileObject[] completeSet = {unhandledRequest, matchedHandler, matchedRequest};
    private final JavaFileObject[] matched = {matchedHandler, matchedRequest};
}
