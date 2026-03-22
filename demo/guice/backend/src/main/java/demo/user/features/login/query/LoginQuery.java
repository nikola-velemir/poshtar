package demo.user.features.login.query;
import demo.user.features.login.response.LoginResponseDTO;
import org.nikola.velemir.poshtar.core.request.Request;

public record LoginQuery(String username, String password) implements Request<LoginResponseDTO> {
}
