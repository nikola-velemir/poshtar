package test.fixtures.rules.semantical.returnTypes.wrapsPrimitives;

import io.github.nikola_velemir.poshtar.core.request.Query;
import io.github.nikola_velemir.poshtar.core.request.Request;

import java.util.List;

public record WrapsPrimitivesRequest() implements Query<List<String>> {
}

