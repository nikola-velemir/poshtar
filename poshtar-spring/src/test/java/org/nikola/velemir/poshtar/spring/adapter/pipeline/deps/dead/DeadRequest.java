package org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.dead;


import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public record DeadRequest() implements Request<Unit> {
}
