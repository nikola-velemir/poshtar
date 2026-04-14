package org.nikola.velemir.poshtar.opt.internal.rules;

import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.opt.processor.ProcessorContext;

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

    @Override
    protected void initErasures(ProcessorContext ctx) {
        var types = ctx.getTypes();

        var elements = ctx.getElements();
        TypeElement behaviourIface = elements.getTypeElement(PipelineBehaviour.class.getCanonicalName());

        if (behaviourIface == null) return;

        behaviourErasure = types.erasure(behaviourIface.asType());
    }

}
