package io.github.nikola_velemir.poshtar.core.request.handler;

import io.github.nikola_velemir.poshtar.core.request.Command;
import io.github.nikola_velemir.poshtar.core.request.VoidCommand;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public interface CommandHandler<TCommand extends Command<TResponse>, TResponse>
        extends RequestHandler<TCommand, TResponse> {
}
