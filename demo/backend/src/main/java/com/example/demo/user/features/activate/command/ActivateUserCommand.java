package com.example.demo.user.features.activate.command;

import org.example.core.request.IRequest;
import org.example.core.types.Unit;

public record ActivateUserCommand(String username) implements IRequest<Unit> {
}
