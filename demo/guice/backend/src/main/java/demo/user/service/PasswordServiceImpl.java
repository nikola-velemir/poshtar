package demo.user.service;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PasswordServiceImpl implements PasswordService {
    private final PasswordEncoder encoder;
    public String hashPassword(String input){
        return encoder.encode(input);
    }

    @Override
    public boolean matches(String input, String actual) {
        return encoder.matches(input, actual);
    }
}
