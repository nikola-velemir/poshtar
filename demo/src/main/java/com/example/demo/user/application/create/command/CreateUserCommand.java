package com.example.demo.user.application.create.command;

import org.example.core.request.IRequest;

public record CreateUserCommand(String name) implements IRequest<Void> {
}
