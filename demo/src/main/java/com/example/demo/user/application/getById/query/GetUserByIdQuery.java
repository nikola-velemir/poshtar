package com.example.demo.user.application.getById.query;

import com.example.demo.user.application.getById.response.GetUserResponseDTO;
import com.example.demo.user.model.User;
import org.example.core.request.IRequest;

public record GetUserByIdQuery(Long id) implements IRequest<GetUserResponseDTO> {
}
