package io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.mock.hierarchy;

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
