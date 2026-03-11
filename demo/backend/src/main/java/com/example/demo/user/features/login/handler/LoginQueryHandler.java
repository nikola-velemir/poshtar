package com.example.demo.user.features.login.handler;

import com.example.demo.user.features.login.query.LoginQuery;
import com.example.demo.user.features.login.response.LoginResponseDTO;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.jspecify.annotations.NonNull;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RequestHandler
public class LoginQueryHandler implements IRequestHandler<LoginQuery, LoginResponseDTO> {
    private final PasswordService passwordService;
    private final UserRepository userRepository;

    @SneakyThrows
    @Override
    public LoginResponseDTO handle(LoginQuery loginQuery) {
        String username = loginQuery.username();
        String password = loginQuery.password();
        User user = userRepository.findUserByUsername(username).orElseThrow(Exception::new);
        boolean passwordsMatch = doPasswordsMatch(password, user.getPassword());
        if(!passwordsMatch)
            throw new AccessDeniedException("Wrong credentials");
        return createResponse(user);

    }

    private static @NonNull LoginResponseDTO createResponse(User user) {
        return new LoginResponseDTO(user.getUsername(), user.getFirstName(), user.getLastName(), user.getId());
    }

    private boolean doPasswordsMatch(String provided, String actual) {
        return passwordService.matches(provided, actual);
    }
}
