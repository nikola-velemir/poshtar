package com.example.demo.user.features.getById.query;

import com.example.demo.user.features.getById.response.GetUserResponseDTO;
import org.nikola.velemir.poshtar.core.request.Request;

public record GetUserByIdQuery(Long id) implements Request<GetUserResponseDTO> {
}
