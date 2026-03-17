package com.example.demo.user.features.register.handler;

import com.example.demo.user.features.register.command.RegisterCommand;
import com.example.demo.user.features.register.notification.RegisterNotification;
import com.example.demo.user.service.PasswordService;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.mediator.Poshtar;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Behaviour
public class RegisterCommandHandler implements RequestHandler<RegisterCommand, Unit> {
    @Autowired
    private final PasswordService passwordService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final Poshtar poshtar;


    public RegisterCommandHandler(UserRepository userRepository, Poshtar poshtar, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.poshtar = poshtar;
        this.passwordService = passwordService;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Unit handle(RegisterCommand command) {
        boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("Is Transaction REALLY Active? " + isActive);
        User user = createUser(command);
        userRepository.save(user);
        poshtar.publish(new RegisterNotification(user.getUsername(), user.getEmail()));
        return Unit.Value;
    }

    private User createUser(RegisterCommand command) {
        String hashedPassword = passwordService.hashPassword(command.password());
        return new User(
                command.username(),
                command.email(),
                hashedPassword,
                command.firstName(),
                command.lastName()
        );
    }
}
