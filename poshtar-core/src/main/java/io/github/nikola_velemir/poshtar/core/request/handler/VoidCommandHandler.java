package io.github.nikola_velemir.poshtar.core.request.handler;

import io.github.nikola_velemir.poshtar.core.request.VoidCommand;
import io.github.nikola_velemir.poshtar.core.types.Unit;

/**
 * Defines a component responsible for processing a specific type of {@link VoidCommand}.
 * <p>
 * Each implementation of this interface is bound to a single void command type and
 * is responsible for executing the logic it contains.
 * Handlers are typically invoked by the {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar}
 * mediator after the void command has passed through the behavior pipeline.
 * </p>
 *
 * @param <TCommand>  The specific type of command this handler processes.
 * @author Nikola Velemir
 * @version ${revision}
 * @since 1.2.0
 */
public  interface VoidCommandHandler<TCommand extends VoidCommand>
        extends RequestHandler<TCommand, Unit> {
}
