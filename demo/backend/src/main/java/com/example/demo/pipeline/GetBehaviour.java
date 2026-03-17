package com.example.demo.pipeline;


import com.example.demo.user.features.getById.query.GetUserByIdQuery;
import com.example.demo.user.features.getById.response.GetUserResponseDTO;
import org.nikola.velemir.poshtar.core.annotations.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.IPipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

@PipelineBehaviour
@Order(2)
public class GetBehaviour implements IPipelineBehaviour<GetUserByIdQuery, GetUserResponseDTO> {
    private static final Logger logger = LoggerFactory.getLogger(GetBehaviour.class);

    @Override
    public GetUserResponseDTO handle(GetUserByIdQuery query, RequestDelegate<GetUserByIdQuery,GetUserResponseDTO> requestDelegate) {
        logger.info("Stigao request za get: " + query.id());
        return requestDelegate.handle(query);
    }
}
