package io.github.nikola_velemir.poshtar.validator.rules.semantical.returnTypes;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static io.github.nikola_velemir.poshtar.validator.rules.TestUtils.compile;

public class NoPrimitiveReturnTypesTest {
    private static class Designated {
        static class Basic {

            static final JavaFileObject response = JavaFileObjects.forResource("test/fixtures/rules/semantical/returnTypes/designated/basic/DesignatedResponse.java");
            static final JavaFileObject request = JavaFileObjects.forResource("test/fixtures/rules/semantical/returnTypes/designated/basic/DesignatedRequest.java");
            static final JavaFileObject handler = JavaFileObjects.forResource("test/fixtures/rules/semantical/returnTypes/designated/basic/DesignatedHandler.java");
            static final JavaFileObject[] set = {request, handler, response};

        }

        static class Wraps {
            static final JavaFileObject response =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/returnTypes/designated/wraps/WrapsDesignatedResponse.java");
            static final JavaFileObject request =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/returnTypes/designated/wraps/WrapsDesignatedRequest.java");
            static final JavaFileObject handler =
                    JavaFileObjects.forResource("test/fixtures/rules/semantical/returnTypes/designated/wraps/WrapsDesignatedHandler.java");
            static final JavaFileObject[] set = {request, handler, response};
        }
    }

    private static class Primitive {
        static class Basic {
            static final JavaFileObject request = JavaFileObjects.forResource("test/fixtures/rules/semantical/returnTypes/primitive/basic/PrimitiveReturnTypeRequest.java");
            static final JavaFileObject handler = JavaFileObjects.forResource("test/fixtures/rules/semantical/returnTypes/primitive/basic/PrimitiveReturnTypeHandler.java");
            static final JavaFileObject[] set = {request, handler};
        }

        static class Wraps {
            static final JavaFileObject request = JavaFileObjects.forResource("test/fixtures/rules/semantical/returnTypes/primitive/wraps/WrapsPrimitivesRequest.java");
            static final JavaFileObject handler = JavaFileObjects.forResource("test/fixtures/rules/semantical/returnTypes/primitive/wraps/WrapsPrimitivesHandler.java");
            static final JavaFileObject[] set = {request, handler};
        }
    }
    @Test
    @DisplayName("Compilation does not warn when wrapping designated return type")
    void shouldNotWarn_whenWrappingDesignatedReturnType() {
        Compilation compilation = compile(Designated.Wraps.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }
    @Test
    @DisplayName("Compilation does not warn when designated return type")
    void shouldNotWarn_whenDesignatedReturnType() {
        Compilation compilation = compile(Designated.Basic.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(0);
    }

    @Test
    @DisplayName("Compilation warns when wrapping primitive return type")
    void shouldWarn_whenWrappingPrimitiveReturnType() {
        Compilation compilation = compile(Primitive.Wraps.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining("[PoshtaR] Request return type 'java.lang.String' is a built-in Java type. It is advisable to use a custom DTO or Unit for better versioning safety.").inFile(Primitive.Wraps.request);
    }

    @Test
    @DisplayName("Compilation warns when primitive return type")
    void shouldWarn_whenPrimitiveReturnType() {
        Compilation compilation = compile(Primitive.Basic.set);

        assertThat(compilation).succeeded();
        assertThat(compilation).hadWarningCount(1);
        assertThat(compilation).hadWarningContaining("[PoshtaR] Request return type 'java.lang.String' is a built-in Java type. It is advisable to use a custom DTO or Unit for better versioning safety.").inFile(Primitive.Basic.request);
    }
}
