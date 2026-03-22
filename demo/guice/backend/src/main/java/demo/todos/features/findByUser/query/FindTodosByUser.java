package demo.todos.features.findByUser.query;

import demo.todos.features.findByUser.response.FindTodoByUserResponseDTO;
import org.nikola.velemir.poshtar.core.request.Request;

import java.util.List;

public record FindTodosByUser(Long userId) implements Request<List<FindTodoByUserResponseDTO>> {
}
