package org.example.core.pipeline;

import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.request.IRequest;

import java.util.List;

public interface IPipelineRegistry{
    <TRequest extends IRequest<TResponse>, TResponse> List<IPipelineBehaviour<TRequest,TResponse>> resolve(Class<TRequest> requestClass);
     void register(IPipelineBehaviour<?, ?> behaviour);
}
