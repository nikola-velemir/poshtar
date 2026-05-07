package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.validate;

import io.github.nikola_velemir.poshtar.core.request.Request;

public record ValidationRequest(int payload) implements Request<Integer> {
}
