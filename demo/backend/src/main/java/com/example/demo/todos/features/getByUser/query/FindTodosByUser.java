package com.example.demo.todos.features.getByUser.query;

import com.example.demo.todos.features.getByUser.response.FindTodoByUserResponseDTO;
import org.example.core.request.IRequest;

import java.util.List;

public record FindTodosByUser(Long userId) implements IRequest<List<FindTodoByUserResponseDTO>> {
}
