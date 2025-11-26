package org.example.impl.pipeline;

import org.example.core.annotations.PipelineBehaviour;
import org.example.impl.request.BasicRequest;
import org.example.impl.request.BasicResponse;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;

@PipelineBehaviour
public class BasicRequestPipeline implements IPipelineBehaviour<BasicRequest, BasicResponse> {
    @Override
    public boolean supports(Class<?> requestType) {
        return requestType.equals(BasicRequest.class);
    }

    @Override
    public BasicResponse handle(BasicRequest basicRequest, RequestDelegate<BasicResponse> next) {
        System.out.println("BasicRequestPipeline.handle");
        return next.handle();
    }
}
