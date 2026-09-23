package test.fixtures.rules.semantical.returnTypes.primitive;

import io.github.nikola_velemir.poshtar.core.request.Query;
import io.github.nikola_velemir.poshtar.core.request.Request;

public record PrimitiveReturnTypeRequest() implements Query<String> {
}
