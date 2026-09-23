package io.github.nikola_velemir.poshtar.validator.rules.architectural.deadPipeline.shadow.alive;

import io.github.nikola_velemir.poshtar.core.request.Command;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record ShadowAliveRequest() implements Command<Unit> {
}

