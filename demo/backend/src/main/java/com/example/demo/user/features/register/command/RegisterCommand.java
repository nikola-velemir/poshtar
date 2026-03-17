package com.example.demo.user.features.register.command;

import org.nikola.velemir.poshtar.core.request.IRequest;
import org.nikola.velemir.poshtar.core.types.Unit;

public record RegisterCommand(String email, String username, String firstName, String lastName,
                              String password) implements IRequest<Unit> {
}
