package com.example.demo.todos.features.findByUser.query;

import com.example.demo.todos.features.findByUser.response.FindTodoByUserResponseDTO;
import org.nikola.velemir.poshtar.core.request.IRequest;

import java.util.List;

public record FindTodosByUser(Long userId) implements IRequest<List<FindTodoByUserResponseDTO>> {
}
