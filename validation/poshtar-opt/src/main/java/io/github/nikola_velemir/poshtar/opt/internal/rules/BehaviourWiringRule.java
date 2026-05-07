package io.github.nikola_velemir.poshtar.opt.internal.rules;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

class BehaviourWiringRule extends WiringRule {

    private TypeMirror behaviourErasure;

    public BehaviourWiringRule() {
        super(Behaviour.class);
    }

    @Override
    protected void validateAnnotationAndImplementation(ProcessorContext ctx, TypeElement typeElement) {
        var types = ctx.getTypes();

        boolean implementsBehaviour = types.isAssignable(types.erasure(typeElement.asType()), behaviourErasure);


        boolean hasAnnotation = this.hasAnnotation(typeElement);

        if (implementsBehaviour && !hasAnnotation) {
            logger.log(ctx.env, "Missing @Behaviour annotation on PipelineBehaviour implementation.", typeElement);
        }

        if (hasAnnotation && !implementsBehaviour) {
            logger.log(ctx.env, "Class annotated with @Behaviour must implement PipelineBehaviour.", typeElement);
        }
    }
    /**
     * Initializes the behavior interface type erasure, used to validate if wiring is correct.
     * @param ctx Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    protected void initErasures(ProcessorContext ctx) {
        var types = ctx.getTypes();

        var elements = ctx.getElements();
        TypeElement behaviourIface = elements.getTypeElement(PipelineBehaviour.class.getCanonicalName());

        if (behaviourIface == null) return;

        behaviourErasure = types.erasure(behaviourIface.asType());
    }

}
