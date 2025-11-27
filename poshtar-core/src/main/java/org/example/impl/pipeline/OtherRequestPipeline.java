package org.example.impl.pipeline;

import org.example.core.annotations.PipelineBehaviour;
import org.example.impl.request.OtherRequest;
import org.example.core.types.Unit;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;

@PipelineBehaviour
public class OtherRequestPipeline implements IPipelineBehaviour<OtherRequest, Unit> {
    @Override
    public boolean supports(Class<?> requestType) {
        return requestType.equals(OtherRequest.class);
    }

    @Override
    public Unit handle(OtherRequest otherRequest, RequestDelegate<Unit> next) {
        System.out.println("OtherRequestPipeline.handle");
        return next.handle();
    }
}
