package io.github.nikola_velemir.poshtar.validator.rules;

import com.google.testing.compile.Compiler;

public class PoshtarProcessorTestBed {
    private final TestUtils.CompilerUtils compilerUtils = new TestUtils.CompilerUtils();

    public TestUtils.CompilerUtils createCompiler(){
        return compilerUtils.createCompiler();
    }
}
