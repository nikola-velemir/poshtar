package io.github.nikola_velemir.poshtar.core.request.handler;

import io.github.nikola_velemir.poshtar.core.request.VoidCommand;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public non-sealed interface VoidCommandHandler<TCommand extends VoidCommand>
        extends RequestHandler<TCommand, Unit> {
}
