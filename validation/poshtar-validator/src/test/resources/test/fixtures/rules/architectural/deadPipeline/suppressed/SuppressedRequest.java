package io.github.nikola_velemir.poshtar.validator.rules.architectural.deadPipeline.supressed;

import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import io.github.nikola_velemir.poshtar.validator.api.annotations.request.SuppressOrphan;

@SuppressOrphan
public record SuppressedRequest() implements Request<Unit> {
}

