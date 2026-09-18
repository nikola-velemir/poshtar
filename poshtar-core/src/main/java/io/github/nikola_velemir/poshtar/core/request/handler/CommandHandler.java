package io.github.nikola_velemir.poshtar.core.request.handler;

import io.github.nikola_velemir.poshtar.core.request.Command;

public interface CommandHandler<TCommand extends Command<TResponse>, TResponse>
        extends RequestHandler<TCommand, TResponse> {
}
