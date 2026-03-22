package demo.todos.features.delete.handler;


import com.google.inject.persist.Transactional;
import demo.todos.features.delete.command.DeleteTodo;
import demo.user.model.User;
import demo.user.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Handler
public class DeleteTodoHandler implements RequestHandler<DeleteTodo, Unit> {
    private final UserRepository userRepository;
    @SneakyThrows
    @Override
    @Transactional
    public Unit handle(DeleteTodo deleteTodo) {
        User user  = userRepository.findUserById(deleteTodo.userId()).orElseThrow(
                ()-> new BadRequestException("User does not exist")
        );
        user.removeTodo(deleteTodo.todoId());
        return Unit.Value;
    }
}
