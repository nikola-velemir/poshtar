package demo.user.features.activate.handler;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import demo.user.features.activate.command.ActivateUserCommand;
import demo.user.model.User;
import demo.user.model.UserStatus;
import demo.user.repository.UserRepository;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Handler
public class ActivateUserCommandHandler implements RequestHandler<ActivateUserCommand, Unit> {
    private final UserRepository userRepository;
    @SneakyThrows
    @Override
    @Transactional
    public Unit handle(ActivateUserCommand command) {
        String username = command.username();

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(()-> new BadRequestException("User does not exist"));

        if(user.getStatus().equals(UserStatus.ACTIVATED))
            throw new BadRequestException("Already activated");

        user.setStatus(UserStatus.ACTIVATED);

        return Unit.Value;
    }
}
