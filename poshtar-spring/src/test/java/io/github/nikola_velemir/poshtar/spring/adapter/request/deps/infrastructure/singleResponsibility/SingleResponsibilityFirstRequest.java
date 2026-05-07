package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.infrastructure.singleResponsibility;

import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record SingleResponsibilityFirstRequest() implements Request<Unit> {
}
