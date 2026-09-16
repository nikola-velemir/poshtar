package io.github.nikola_velemir.poshtar.validator.rules;

import com.google.testing.compile.Compilation;
import io.github.nikola_velemir.poshtar.validator.processor.PoshtarValidationProcessor;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.Compiler.javac;

public class TestUtils {
    // Helper method to keep test bodies ultra-clean
    public static Compilation compile(JavaFileObject... sources) {
        return javac()
                .withProcessors(new PoshtarValidationProcessor())
                .compile(sources);
    }
}
