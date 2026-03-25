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
        // This rule operates on the whole round, not individual handlers.
    }

    @Override
    public void validateRound(RoundEnvironment roundEnv, RuleContext ctx) {
        // 1. Get the "Blacklist" from our persistent registry
        Set<String> forbiddenHandlers = ctx.getAllKnownHandlers();
        if (forbiddenHandlers.isEmpty()) return;

        // 2. Scan every class being compiled in this round
        for (Element root : roundEnv.getRootElements()) {
            if (root.getKind() != ElementKind.CLASS) continue;

            TypeElement clazz = (TypeElement) root;

            // Skip the Mediator itself (it needs to hold references)
            if (clazz.getQualifiedName().contentEquals(MEDIATOR_FQN)) continue;

            checkClassBody(clazz, forbiddenHandlers, ctx);
        }
    }

    private void checkClassBody(TypeElement clazz, Set<String> forbidden, RuleContext ctx) {
        for (Element enclosed : clazz.getEnclosedElements()) {

            // TYPE A: Field Injection (@Autowired/@Inject MyHandler h)
            if (enclosed.getKind() == ElementKind.FIELD) {
                VariableElement field = (VariableElement) enclosed;
                if (isInjectionAnnotated(field) && isForbiddenType(field.asType(), forbidden, ctx)) {
                    logViolation(field, ctx);
                }
            }

            // TYPE B: Constructor Injection (public Service(MyHandler h))
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructor = (ExecutableElement) enclosed;
                for (VariableElement param : constructor.getParameters()) {
                    if (isForbiddenType(param.asType(), forbidden, ctx)) {
                        logViolation(param, ctx);
                    }
                }
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
                "PoshtaR SECURITY VIOLATION: Handlers cannot be injected or manually managed. " +
                        "Use 'Poshtar.send(request)' to interact with this logic.",
                target
        );
    }
}