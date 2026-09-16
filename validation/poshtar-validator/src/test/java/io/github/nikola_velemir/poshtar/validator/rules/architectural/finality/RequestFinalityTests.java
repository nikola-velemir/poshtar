package io.github.nikola_velemir.poshtar.validator.rules.architectural.finality;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static io.github.nikola_velemir.poshtar.validator.rules.TestUtils.compile;

public class RequestFinalityTests {


    @Test
    @DisplayName("Compilation fails when a request is not final")
    void shouldFailCompilation_whenRequestNotFinal() {
        Compilation compilation = compile(NonFinal.set);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("Finality Violated!")
                .inFile(NonFinal.request);

        assertThat(compilation)
                .hadErrorContaining("NonFinalRequest")
                .inFile(NonFinal.request);

    }

    @Test
    @DisplayName("Compilation fails when a request is not record")
    void shouldFailCompilation_whenRequestNotRecord() {
        Compilation compilation = compile(NonRecord.set);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("Finality Violated!")
                .inFile(NonRecord.request);

        assertThat(compilation)
                .hadErrorContaining("NonRecordRequest")
                .inFile(NonRecord.request);
    }

    @Test
    @DisplayName("Compilation passes for final request")
    void shouldPassCompilation_whenRequestIsFinal() {
        Compilation compilation = compile(Final.set);

        assertThat(compilation).succeeded();
    }

    @Test
    @DisplayName("Compilation passes for record request")
    void shouldPassCompilation_whenRequestIsRecord() {
        Compilation compilation = compile(Record.set);

        assertThat(compilation).succeeded();
    }

    private static class NonFinal {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/request/NonFinalRequest.java");

        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/request/NonFinalRequestHandler.java");
        static final JavaFileObject[] set = {request, handler};

    }

    private static class Final {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/request/FinalRequest.java");

        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/request/FinalRequestHandler.java");
        static final JavaFileObject[] set = {request, handler};

    }

    private static class Record {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/request/RecordRequest.java");

        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/request/RecordRequestHandler.java");
        static final JavaFileObject[] set = {request, handler};

    }

    private static class NonRecord {
        static final JavaFileObject request =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/request/NonRecordRequest.java");

        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/request/NonRecordRequestHandler.java");
        static final JavaFileObject[] set = {request, handler};

    }
}
