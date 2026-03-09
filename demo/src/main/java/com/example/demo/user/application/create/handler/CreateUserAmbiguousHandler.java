package com.example.demo.user.application.create.handler;

import com.example.demo.user.application.create.command.CreateUserCommand;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

public class CreateUserAmbiguousHandler implements IRequestHandler<CreateUserCommand, Void> {
    @Autowired
    private final UserRepository userRepository;

    public CreateUserAmbiguousHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Void handle(CreateUserCommand createUserCommand) {
        String name = createUserCommand.name();
        User user = new User(name);
        userRepository.save(user);
        return Void.TYPE.cast(null);
    }

    private Long generateId() {
        Random random = new Random();
        return random.nextLong();
    }
}
