package demo.user.features.getById.query;

import demo.user.features.getById.response.GetUserResponseDTO;
import org.nikola.velemir.poshtar.core.request.Request;

public record GetUserByIdQuery(Long id) implements Request<GetUserResponseDTO> {
}
