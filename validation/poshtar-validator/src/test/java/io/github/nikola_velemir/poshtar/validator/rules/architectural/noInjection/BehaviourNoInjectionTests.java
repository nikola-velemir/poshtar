package io.github.nikola_velemir.poshtar.validator.rules.architectural.noInjection;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static io.github.nikola_velemir.poshtar.validator.rules.TestUtils.compile;

public class BehaviourNoInjectionTests {
    static final JavaFileObject request =
            JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/behaviour/InjectedRequest.java");
    static final JavaFileObject behaviour =
            JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/behaviour/InjectedRequestBehaviour.java");
    static final JavaFileObject handler =
            JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/behaviour/InjectedRequestHandler.java");
    static final JavaFileObject failingConsumer =
            JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/behaviour/InjectedRequestFailingConsumer.java");

    static final JavaFileObject validConsumer =
            JavaFileObjects.forResource("test/fixtures/rules/architectural/noInjection/behaviour/InjectedRequestValidConsumer.java");
    static final JavaFileObject[] validSet = {request, handler, behaviour, validConsumer};
    static final JavaFileObject[] failingSet = {request, handler, behaviour, failingConsumer};

    @Test
    @DisplayName("Compilation fails when notification components injected but not overruled!")
    void shouldFailCompilation_whenNotificationComponentsNotOverruled() {
        Compilation compilation = compile(failingSet);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("[PoshtaR] PoshtaR VIOLATION: Behaviours cannot be injected, set thru methods or constructor, or manually managed. Use 'Poshtar.send(request)'")
                .inFile(failingConsumer);
    }

    @Test
    @DisplayName("Compilation passes when notification components injected and overruled!")
    void shouldFailCompilation_whenNotificationComponentsOverruled() {
        Compilation compilation = compile(validSet);

        assertThat(compilation).succeeded();
    }
}
