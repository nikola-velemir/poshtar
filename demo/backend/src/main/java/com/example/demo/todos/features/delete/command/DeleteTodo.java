package com.example.demo.todos.features.delete.command;


import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public record DeleteTodo(Long userId, Long todoId) implements Request<Unit> {
}
