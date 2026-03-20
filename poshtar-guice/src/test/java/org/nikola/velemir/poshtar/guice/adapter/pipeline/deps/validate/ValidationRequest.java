package org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.validate;

import org.nikola.velemir.poshtar.core.request.Request;

public record ValidationRequest(int payload) implements Request<Integer> {
}
