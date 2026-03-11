package com.example.demo.user.features.register.command;

import org.example.core.request.IRequest;
import org.example.core.types.Unit;

public record RegisterCommand(String email, String username, String firstName, String lastName,
                              String password) implements IRequest<Unit> {
}
