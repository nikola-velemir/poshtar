package com.example.demo.user.service;

public interface PasswordService {
    String hashPassword(String input);
    boolean matches(String input, String actual);
}
