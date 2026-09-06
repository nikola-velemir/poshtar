package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.mock.basic;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;

@Behaviour
public class BasicMockPipeline implements PipelineBehaviour<BasicMockRequest, String> {
    @Override
    public String handle(BasicMockRequest basicMockRequest, RequestDelegate<BasicMockRequest, String> requestDelegate) {
        return requestDelegate.handle(basicMockRequest);
    }
}
