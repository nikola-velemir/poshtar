package com.example.demo.todos.features.create.command;

import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public record CreateTodo(Long userId, String title, String description) implements Request<Unit> {
}
