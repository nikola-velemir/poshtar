package com.example.demo.user.features.register.handler;

import com.example.demo.user.features.register.command.RegisterCommand;
import com.example.demo.user.service.PasswordService;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequestHandler
public class RegisterCommandHandler implements IRequestHandler<RegisterCommand, Unit> {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordService passwordService;

    public RegisterCommandHandler(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Unit handle(RegisterCommand command) {
        User user = createUser(command);
        userRepository.save(user);
        return Unit.Value;
    }

    private User createUser(RegisterCommand command) {
        String hashedPassword = passwordService.hashPassword(command.password());
        return new User(
                command.username(),
                hashedPassword,
                command.firstName(),
                command.lastName()
        );
    }
}
