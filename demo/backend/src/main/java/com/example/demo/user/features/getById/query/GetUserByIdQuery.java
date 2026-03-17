package com.example.demo.user.features.getById.query;

import com.example.demo.user.features.getById.response.GetUserResponseDTO;
import org.nikola.velemir.poshtar.core.request.IRequest;

public record GetUserByIdQuery(Long id) implements IRequest<GetUserResponseDTO> {
}
