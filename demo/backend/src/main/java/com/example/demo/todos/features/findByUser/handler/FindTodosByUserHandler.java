package com.example.demo.todos.features.findByUser.handler;

import com.example.demo.todos.features.findByUser.query.FindTodosByUser;
import com.example.demo.todos.features.findByUser.response.FindTodoByUserResponseDTO;
import com.example.demo.todos.model.TodoItem;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@RequestHandler
public class FindTodosByUserHandler implements IRequestHandler<FindTodosByUser, List<FindTodoByUserResponseDTO>> {
    private final UserRepository userRepository;
    @Override
    public List<FindTodoByUserResponseDTO> handle(FindTodosByUser query) {
        Long userId = query.userId();
        User user = userRepository.getUserById(userId).orElseThrow(RuntimeException::new);
        var todos = user.getTodos();
        return createResponse(todos);
    }

    private static @NonNull List<FindTodoByUserResponseDTO> createResponse(List<TodoItem> todos) {
        return todos.stream()
                .sorted(Comparator.comparing(TodoItem::getTitle))
                .map(t -> new FindTodoByUserResponseDTO(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus().toString()
        )).toList();
    }
}
