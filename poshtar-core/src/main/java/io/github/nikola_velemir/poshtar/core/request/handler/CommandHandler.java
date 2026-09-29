package io.github.nikola_velemir.poshtar.core.request.handler;

import io.github.nikola_velemir.poshtar.core.request.Command;

/**
 * Defines a component responsible for processing a specific type of {@link Command}.
 * <p>
 * Each implementation of this interface is bound to a single command type and
 * is responsible for executing the logic it contains.
 * Handlers are typically invoked by the {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar}
 * mediator after the command has passed through the behavior pipeline.
 * </p>
 *
 * @param <TCommand>  The specific type of command this handler processes.
 * @param <TResponse> The type of response produced by this handler.
 * @author Nikola Velemir
 * @version ${revision}
 * @since 1.2.0
 */
public  interface CommandHandler<TCommand extends Command<TResponse>, TResponse>
        extends RequestHandler<TCommand, TResponse> {
}
