package demo.todos.features.updateStatus.handler;


import com.google.inject.persist.Transactional;
import demo.todos.features.updateStatus.command.UpdateStatusCommand;
import demo.todos.model.TodoItem;
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
public class UpdateStatusCommandHandler implements RequestHandler<UpdateStatusCommand, Unit> {
    private final UserRepository userRepository;

    @SneakyThrows
    @Override
    @Transactional
    public Unit handle(UpdateStatusCommand command) {
        User user = userRepository.findUserById(command.userId()).orElseThrow(
                () -> new BadRequestException("User does not exist")
        );
        Long todoId = command.todoId();
        TodoItem todo = user.getTodos()
                .stream()
                .filter(t -> t.getId().equals(todoId))
                .findFirst().orElseThrow(() -> new BadRequestException("Todo does not exist"));
        todo.setStatus(command.status());
        return Unit.Value;
    }
}
