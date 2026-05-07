package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.global;


import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record GlobalPipelineTestRequest() implements Request<Unit> {
}
