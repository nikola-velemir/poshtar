package demo.todos.features.findByUser.handler;


import demo.todos.features.findByUser.query.FindTodosByUser;
import demo.todos.features.findByUser.response.FindTodoByUserResponseDTO;
import demo.todos.model.TodoItem;
import demo.user.model.User;
import demo.user.repository.UserRepository;
import jakarta.inject.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Handler
public class FindTodosByUserHandler implements RequestHandler<FindTodosByUser, List<FindTodoByUserResponseDTO>> {
    private final UserRepository userRepository;
    @Override
    public List<FindTodoByUserResponseDTO> handle(FindTodosByUser query) {
        Long userId = query.userId();
        User user = userRepository.findUserById(userId).orElseThrow(RuntimeException::new);
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
