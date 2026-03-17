package com.example.demo.user.features.login.query;


import com.example.demo.user.features.login.response.LoginResponseDTO;
import org.nikola.velemir.poshtar.core.request.IRequest;

public record LoginQuery(String username, String password) implements IRequest<LoginResponseDTO> {
}
