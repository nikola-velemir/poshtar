package com.example.demo.user.features.login.query;

import com.example.demo.user.features.login.response.LoginResponseDTO;
import org.example.core.request.IRequest;

public record LoginQuery(String username, String password) implements IRequest<LoginResponseDTO> {
}
