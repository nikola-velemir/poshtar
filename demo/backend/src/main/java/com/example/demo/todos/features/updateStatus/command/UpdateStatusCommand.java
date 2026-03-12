package com.example.demo.todos.features.updateStatus.command;

import com.example.demo.todos.model.TodoStatus;
import org.example.core.request.IRequest;
import org.example.core.types.Unit;

public record UpdateStatusCommand(Long userId, Long todoId,TodoStatus status) implements IRequest<Unit> {
}
