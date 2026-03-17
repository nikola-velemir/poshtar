package com.example.demo.user.features.register.handler;

import com.example.demo.user.features.register.command.RegisterCommand;
import com.example.demo.user.repository.UserRepository;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

public class RegisterAmbiguousHandler implements RequestHandler<RegisterCommand, Unit> {
    @Autowired
    private final UserRepository userRepository;

    public RegisterAmbiguousHandler(UserRepository userRepository) {
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
