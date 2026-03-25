package org.nikola.velemir.poshtar.opt.rules;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.Set;

public class NoInjectionRule implements Rule {

    private static final String MEDIATOR_FQN = "org.nikola.velemir.poshtar.core.mediator.Poshtar";

    @Override
    public void validate(TypeElement element, RuleContext ctx) {
    }

    @Override
    public void validateRound(RoundEnvironment roundEnv, RuleContext ctx) {
        Set<String> forbiddenHandlers = ctx.getAllKnownHandlers();
        if (forbiddenHandlers.isEmpty()) return;

        for (Element root : roundEnv.getRootElements()) {
            if (root.getKind() != ElementKind.CLASS) continue;

            TypeElement clazz = (TypeElement) root;

            if (clazz.getQualifiedName().contentEquals(MEDIATOR_FQN)) continue;

            checkClassBody(clazz, forbiddenHandlers, ctx);
        }
    }

    private void checkClassBody(TypeElement clazz, Set<String> forbidden, RuleContext ctx) {
        for (Element enclosed : clazz.getEnclosedElements()) {

            validateFieldInjection(forbidden, ctx, enclosed);

            validateConstructorInjection(forbidden, ctx, enclosed);
        }
    }

    private void validateConstructorInjection(Set<String> forbidden, RuleContext ctx, Element enclosed) {
        if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
            ExecutableElement constructor = (ExecutableElement) enclosed;
            for (VariableElement param : constructor.getParameters()) {
                if (isForbiddenType(param.asType(), forbidden, ctx)) {
                    logViolation(param, ctx);
                }
            }
        }
    }

    private void validateFieldInjection(Set<String> forbidden, RuleContext ctx, Element enclosed) {
        if (enclosed.getKind() == ElementKind.FIELD) {
            VariableElement field = (VariableElement) enclosed;
            if (isInjectionAnnotated(field) && isForbiddenType(field.asType(), forbidden, ctx)) {
                logViolation(field, ctx);
            }
        }
    }

    private boolean isInjectionAnnotated(Element e) {
        return e.getAnnotationMirrors().stream()
                .map(a -> a.getAnnotationType().asElement().getSimpleName().toString())
                .anyMatch(name -> name.equals("Autowired") || name.equals("Inject") || name.equals("Resource"));
    }

    private boolean isForbiddenType(TypeMirror type, Set<String> forbidden, RuleContext ctx) {
        // Convert the type mirror to a qualified string (e.g., "com.app.MyHandler")
        String typeName = ctx.env.getTypeUtils().erasure(type).toString();
        return forbidden.contains(typeName);
    }

    private void logViolation(Element target, RuleContext ctx) {
        ctx.env.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                "PoshtaR VIOLATION: Handlers cannot be injected or manually managed. " +
                        "Use 'Poshtar.send(request)' to interact with this logic.",
                target
        );
    }
}