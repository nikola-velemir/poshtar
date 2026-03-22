package demo.user.features.activate.command;


import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public record ActivateUserCommand(String username) implements Request<Unit> {
}
