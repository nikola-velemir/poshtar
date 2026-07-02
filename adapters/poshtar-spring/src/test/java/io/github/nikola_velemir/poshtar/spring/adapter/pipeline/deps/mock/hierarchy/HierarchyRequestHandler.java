package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.mock.hierarchy;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class HierarchyRequestHandler implements RequestHandler<HierarchyRequest, String> {
    @Override
    public String handle(HierarchyRequest hierarchyRequest) {
        return "Good :)";
    }
}
