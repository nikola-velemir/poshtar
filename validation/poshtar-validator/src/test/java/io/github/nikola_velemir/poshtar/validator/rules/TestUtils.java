package io.github.nikola_velemir.poshtar.validator.rules;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import io.github.nikola_velemir.poshtar.validator.processor.PoshtarValidationProcessor;
import org.jspecify.annotations.NonNull;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.Compiler.javac;

public class TestUtils {
    public static class CompilerUtils {

        private Compiler compiler;

        public CompilerUtils createCompiler() {
            compiler = javac();
            return this;
        }

        public Compiler withProcessors(
                javax.annotation.processing.Processor... processors
        ) {
            return compiler.withProcessors(processors);
        }

        @NonNull
        public Compiler withDefaultProcessor(

        ) {
            return compiler.withProcessors(new PoshtarValidationProcessor());
        }

        // Helper method to keep test bodies ultra-clean
        public Compilation compile(JavaFileObject... sources) {
            return compiler
                    .compile(sources);
        }
    }
}
