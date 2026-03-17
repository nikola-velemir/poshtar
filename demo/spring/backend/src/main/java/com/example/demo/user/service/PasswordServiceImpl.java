package com.example.demo.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
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
