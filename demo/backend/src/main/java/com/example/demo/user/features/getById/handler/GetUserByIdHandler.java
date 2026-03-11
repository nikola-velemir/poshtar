package com.example.demo.user.features.getById.handler;

import com.example.demo.user.features.getById.query.GetUserByIdQuery;
import com.example.demo.user.features.getById.response.GetUserResponseDTO;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;

@RequestHandler
public class GetUserByIdHandler implements IRequestHandler<GetUserByIdQuery, GetUserResponseDTO> {
    @Autowired
    private final UserRepository repository;

    public GetUserByIdHandler(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public GetUserResponseDTO handle(GetUserByIdQuery query) {
        try {
            User user = repository.findUserById(query.id())
                    .orElseThrow(() -> new Exception("aa"));

            return new GetUserResponseDTO(user.getId(), user.getUsername());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
