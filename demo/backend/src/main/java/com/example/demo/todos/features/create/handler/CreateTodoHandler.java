package com.example.demo.todos.features.create.handler;

import com.example.demo.todos.features.create.command.CreateTodo;
import com.example.demo.todos.model.TodoItem;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@RequestHandler
public class CreateTodoHandler implements IRequestHandler<CreateTodo, Unit> {
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
