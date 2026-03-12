package com.example.demo.todos.features.updateStatus.handler;

import com.example.demo.todos.features.updateStatus.command.UpdateStatusCommand;
import com.example.demo.todos.model.TodoItem;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@RequestHandler
public class UpdateStatusCommandHandler implements IRequestHandler<UpdateStatusCommand, Unit> {
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
