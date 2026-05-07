package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.infrastructure.singleResponsibility;


import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public interface BehaviourMaskingInterface extends
        PipelineBehaviour<SingleResponsibilityFirstRequest, Unit> {
}
