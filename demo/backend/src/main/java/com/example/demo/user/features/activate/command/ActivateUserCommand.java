package com.example.demo.user.features.activate.command;


import org.nikola.velemir.poshtar.core.request.IRequest;
import org.nikola.velemir.poshtar.core.types.Unit;

public record ActivateUserCommand(String username) implements IRequest<Unit> {
}
