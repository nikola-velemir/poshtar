package demo.pipeline;


import com.google.inject.Inject;
import demo.user.features.getById.query.GetUserByIdQuery;
import demo.user.features.getById.response.GetUserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Behaviour
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GetBehaviour implements PipelineBehaviour<GetUserByIdQuery, GetUserResponseDTO> {
    private static final Logger logger = LoggerFactory.getLogger(GetBehaviour.class);

    @Override
    public GetUserResponseDTO handle(GetUserByIdQuery query, RequestDelegate<GetUserByIdQuery,GetUserResponseDTO> requestDelegate) {
        logger.info("Stigao request za get: " + query.id());
        return requestDelegate.handle(query);
    }
}
