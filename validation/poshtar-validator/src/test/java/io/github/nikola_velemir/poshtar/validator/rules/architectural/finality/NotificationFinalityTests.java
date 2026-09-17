package io.github.nikola_velemir.poshtar.validator.rules.architectural.finality;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import io.github.nikola_velemir.poshtar.validator.processor.PoshtarValidationProcessor;
import io.github.nikola_velemir.poshtar.validator.rules.PoshtarProcessorTestBed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class NotificationFinalityTests extends PoshtarProcessorTestBed {


    @Test
    @DisplayName("Compilation fails when a notification is not final")
    void shouldFailCompilation_whenNotificationNotFinal() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(NonFinal.set);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("Finality Violated!")
                .inFile(NonFinal.notification);

        assertThat(compilation)
                .hadErrorContaining("NonFinalNotification")
                .inFile(NonFinal.notification);

    }

    @Test
    @DisplayName("Compilation fails when a notification is not record")
    void shouldFailCompilation_whenNotificationNotRecord() {

        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(NonRecord.set);

        assertThat(compilation).failed();

        assertThat(compilation)
                .hadErrorContaining("Finality Violated!")
                .inFile(NonRecord.notification);

        assertThat(compilation)
                .hadErrorContaining("NonRecordNotification")
                .inFile(NonRecord.notification);
    }

    @Test
    @DisplayName("Compilation passes for final notification")
    void shouldPassCompilation_whenNotificationIsFinal() {
        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Final.set);

        assertThat(compilation).succeeded();
    }

    @Test
    @DisplayName("Compilation passes for record notification")
    void shouldPassCompilation_whenNotificationIsRecord() {

        Compilation compilation = createCompiler()
                .withProcessors(createProcessor())
                .compile(Record.set);

        assertThat(compilation).succeeded();
    }

    private static class NonFinal {
        static final JavaFileObject notification =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/notification/NonFinalNotification.java");

        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/notification/NonFinalNotificationHandler.java");
        static final JavaFileObject[] set = {notification, handler};

    }

    private static class Final {
        static final JavaFileObject notification =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/notification/FinalNotification.java");

        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/notification/FinalNotificationHandler.java");
        static final JavaFileObject[] set = {notification, handler};

    }

    private static class Record {
        static final JavaFileObject notification =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/notification/RecordNotification.java");

        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/notification/RecordNotificationHandler.java");
        static final JavaFileObject[] set = {notification, handler};

    }

    private static class NonRecord {
        static final JavaFileObject notification =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/notification/NonRecordNotification.java");

        static final JavaFileObject handler =
                JavaFileObjects.forResource("test/fixtures/rules/architectural/finality/notification/NonRecordNotificationHandler.java");
        static final JavaFileObject[] set = {notification, handler};

    }
    private PoshtarValidationProcessor createProcessor(){
        var validator = new RuleValidatorProvider.Notification();
        return new PoshtarValidationProcessor(validator);
    }
}