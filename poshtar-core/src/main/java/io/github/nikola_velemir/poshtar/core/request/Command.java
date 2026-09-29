package io.github.nikola_velemir.poshtar.core.request;
/**
 * Defines a command that can be dispatched to the mediator.
 * <p>
 * Class implementing this interface represents a command, to be handled by its specific command handler.
 * {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar}'s {@code send} method processes this command, and returns an object of {@code TResponse} type.
 * </p>
 *
 * <p>In essence, commands should not return a designated response, defined by CQRS principle.
 * Altough, interface allows the concrete return type if use case does require such logic.
 * </p>
 *
 * @param <TResponse> The type of the response expected after processing this command.
 * @author Nikola Velemir
 * @version ${revision}
 * @since 1.2.0
 */
public  interface Command<TResponse> extends Request<TResponse>{
}
