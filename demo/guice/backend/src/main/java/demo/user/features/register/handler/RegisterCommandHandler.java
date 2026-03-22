package demo.user.features.register.handler;


import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import demo.user.features.register.command.RegisterCommand;
import demo.user.features.register.notification.RegisterNotification;
import demo.user.model.User;
import demo.user.repository.UserRepository;
import demo.user.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.mediator.Poshtar;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Handler
public class RegisterCommandHandler implements RequestHandler<RegisterCommand, Unit> {

    private final PasswordService passwordService;
    private final UserRepository userRepository;
    private final Poshtar poshtar;
    private final ExecutorService executorService; // Inject a managed thread pool

    @Override
    @Transactional
    public Unit handle(RegisterCommand command) {
        User user = createUser(command);
        userRepository.save(user);
        executorService.submit(() -> {
            try {
                poshtar.publish(new RegisterNotification(user.getUsername(), user.getEmail()));
            } catch (Exception e) {
                System.out.println("Failed");
            }
        });
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
