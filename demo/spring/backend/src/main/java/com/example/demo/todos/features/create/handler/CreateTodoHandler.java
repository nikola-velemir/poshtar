package com.example.demo.todos.features.create.handler;

import com.example.demo.todos.features.create.command.CreateTodo;
import com.example.demo.todos.model.TodoItem;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Handler
public class CreateTodoHandler implements RequestHandler<CreateTodo, Unit> {
    private final UserRepository userRepository;
    @Override
    @Transactional
    public Unit handle(CreateTodo createTodo) {
        Long userId = createTodo.userId();
        User user = userRepository.getUserById(userId).orElseThrow(RuntimeException::new);
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
