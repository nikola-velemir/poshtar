package com.example.demo.todos.features.delete.command;

import org.example.core.request.IRequest;
import org.example.core.types.Unit;

public record DeleteTodo(Long userId, Long todoId) implements IRequest<Unit> {
}
