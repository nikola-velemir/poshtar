package demo.todos.features.create.handler;
import com.google.inject.persist.Transactional;
import demo.todos.features.create.command.CreateTodo;
import demo.todos.model.TodoItem;
import demo.user.model.User;
import demo.user.repository.UserRepository;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Handler
public class CreateTodoHandler implements RequestHandler<CreateTodo, Unit> {
    private final UserRepository userRepository;
    @Override
    @Transactional
    public Unit handle(CreateTodo createTodo) {
        Long userId = createTodo.userId();
        User user = userRepository.findUserById(userId).orElseThrow(RuntimeException::new);
        TodoItem todo = createTodoItem(createTodo, user);
        user.addTodo(todo);

        return Unit.Value;
    }
    private TodoItem createTodoItem(CreateTodo command, User user){
        return new TodoItem(
                command.title(),
                command.description(),
                user
        );
    }
}
