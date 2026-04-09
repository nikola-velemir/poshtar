package org.nikola.velemir.poshtar.spring.adapter.request.deps.infrastructure.singleResponsibility;

import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.types.Unit;

public interface BehaviourMaskingInterface extends
        PipelineBehaviour<SingleResponsibilityFirstRequest, Unit> {
}
