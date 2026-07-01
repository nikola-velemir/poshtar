package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.mock.hierarchy;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;

@Behaviour
public class HierarchyFirstBehaviour implements PipelineBehaviour<HierarchyRequest, String> {
    @Override
    public String handle(HierarchyRequest hierarchyRequest, RequestDelegate<HierarchyRequest, String> requestDelegate) {
        return requestDelegate.handle(hierarchyRequest);
    }
}
