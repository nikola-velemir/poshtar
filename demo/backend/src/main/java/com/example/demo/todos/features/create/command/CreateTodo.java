package com.example.demo.todos.features.create.command;

import org.example.core.request.IRequest;
import org.example.core.types.Unit;

public record CreateTodo(Long userId, String title, String description) implements IRequest<Unit> {
}
