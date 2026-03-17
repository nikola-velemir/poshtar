package com.example.demo.user.features.activate.handler;

import com.example.demo.user.features.activate.command.ActivateUserCommand;
import com.example.demo.user.model.User;
import com.example.demo.user.model.UserStatus;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
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
