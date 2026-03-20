package org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.global;
import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public record GlobalPipelineTestRequest() implements Request<Unit> {
}
