package org.example.impl.pipeline;

import org.example.core.pipeline.IPipelineRegistry;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.request.IRequest;

import java.util.ArrayList;
import java.util.List;

public class PipelineRegistry implements IPipelineRegistry{
    private final List<IPipelineBehaviour<?, ?>> behaviors = new ArrayList<>();
    public PipelineRegistry(){}


    @Override
    public <TRequest extends IRequest<TResponse>, TResponse> List<IPipelineBehaviour<TRequest, TResponse>> resolve(Class<TRequest> requestClass) {
        List<IPipelineBehaviour<TRequest, TResponse>> foundBehaviours =
                this.behaviors.stream()

                        .filter(b->b.supports(requestClass))
                        .map(b->(IPipelineBehaviour<TRequest,TResponse>) b)
                        .toList();
        return foundBehaviours;
    }

    @Override
    public void register(IPipelineBehaviour<?, ?> behaviour){
        this.behaviors.add(behaviour);
    }
}
