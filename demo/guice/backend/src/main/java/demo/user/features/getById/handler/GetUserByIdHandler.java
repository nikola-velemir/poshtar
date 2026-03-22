package demo.user.features.getById.handler;


import com.google.inject.Inject;
import demo.user.features.getById.query.GetUserByIdQuery;
import demo.user.features.getById.response.GetUserResponseDTO;
import demo.user.model.User;
import demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Handler
public class GetUserByIdHandler implements RequestHandler<GetUserByIdQuery, GetUserResponseDTO> {

    private final UserRepository repository;

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
