package io.github.nikola_velemir.poshtar.core.request;

import io.github.nikola_velemir.poshtar.core.types.Unit;
/**
 * Defines a command that can be dispatched to the mediator.
 * <p>
 * Class implementing this interface represents a command, returning only {@link Unit}, to be handled by its specific command handler.
 * {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar}'s {@code send} method processes this command, and returns an object of {@code TResponse} type.
 * </p>
 *
 * <p>This interface is created to follow CQRS principe, where commands should not return any response type after processing. 
 * Therefore {@link Unit} is used as a response type.</p>
 *
 * @author Nikola Velemir
 * @version ${revision}
 * @since 1.2.0
 */
public  interface VoidCommand extends Request<Unit>{
}
