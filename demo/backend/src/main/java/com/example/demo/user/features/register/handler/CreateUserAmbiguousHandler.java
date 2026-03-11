package com.example.demo.user.features.register.handler;

import com.example.demo.user.features.register.command.RegisterCommand;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

public class CreateUserAmbiguousHandler implements IRequestHandler<RegisterCommand, Unit> {
    @Autowired
    private final UserRepository userRepository;

    public CreateUserAmbiguousHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Unit handle(RegisterCommand createUserCommand) {

        return Unit.Value;
    }

    private Long generateId() {
        Random random = new Random();
        return random.nextLong();
    }
}
