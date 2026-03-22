package demo.user.features.login.handler;


import com.google.inject.Inject;
import demo.user.features.login.query.LoginQuery;
import demo.user.features.login.response.LoginResponseDTO;
import demo.user.model.User;
import demo.user.repository.UserRepositoryImpl;
import demo.user.service.PasswordService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Handler
public class LoginQueryHandler implements RequestHandler<LoginQuery, LoginResponseDTO> {
    private final PasswordService passwordService;
    private final UserRepositoryImpl userRepository;

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
