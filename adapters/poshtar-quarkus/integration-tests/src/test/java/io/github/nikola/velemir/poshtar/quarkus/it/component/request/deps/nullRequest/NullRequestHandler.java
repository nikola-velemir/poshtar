package io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.nullRequest;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class NullRequestHandler implements RequestHandler<NullRequest, Unit> {
    @Override
    public Unit handle(NullRequest nullRequest) {
        return null;
    }
}
