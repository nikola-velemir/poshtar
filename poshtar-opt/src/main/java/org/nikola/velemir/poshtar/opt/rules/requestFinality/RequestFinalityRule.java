package org.nikola.velemir.poshtar.opt.rules.requestFinality;

import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class RequestFinalityRule implements Rule {
    @Override
    public void validate(RoundEnvironment roundEnv, RuleContext ctx) {

        for (var entry : ctx.getRegistry().entrySet()) {
            String requestFqn = (String) entry.getValue();
            if ("BEHAVIOUR".equals(requestFqn)) continue;

            TypeElement element = ctx.env.getElementUtils().getTypeElement(requestFqn);

            boolean isRecord = element.getKind() == ElementKind.RECORD;
            boolean isFinal = element.getModifiers().contains(Modifier.FINAL);

            if (!isRecord && !isFinal) logError(ctx, requestFqn);

        }
    }

    private static void logError(RuleContext ctx, String requestFqn) {
        ctx.env.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                String.format("PoshtaR: Finality Violated! Request '%s' must be final or a record!",
                        requestFqn)
        );
    }
}
