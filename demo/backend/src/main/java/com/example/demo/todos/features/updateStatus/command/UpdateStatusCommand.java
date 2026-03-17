package com.example.demo.todos.features.updateStatus.command;

import com.example.demo.todos.model.TodoStatus;
import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public record UpdateStatusCommand(Long userId, Long todoId,TodoStatus status) implements Request<Unit> {
}
