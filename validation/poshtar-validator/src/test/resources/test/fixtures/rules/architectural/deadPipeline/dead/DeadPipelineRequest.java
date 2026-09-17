package test.fixtures.rules.architectural.deadPipeline;

import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record DeadPipelineRequest() implements Request<Unit> {
}
