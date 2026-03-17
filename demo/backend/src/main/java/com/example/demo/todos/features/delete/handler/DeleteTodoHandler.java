package com.example.demo.todos.features.delete.handler;

import com.example.demo.todos.features.delete.command.DeleteTodo;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@RequestHandler
public class DeleteTodoHandler implements IRequestHandler<DeleteTodo, Unit> {
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
