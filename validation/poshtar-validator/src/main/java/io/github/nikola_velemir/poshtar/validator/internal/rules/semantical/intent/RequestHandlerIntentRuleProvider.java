package io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.intent;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleKind;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleProvider;

import javax.annotation.processing.RoundEnvironment;
import java.util.List;

public class RequestHandlerIntentRuleProvider implements RuleProvider {


    @Override
    public RuleKind getKind() {
        return RuleKind.SEMANTICAL;
    }

    @Override
    public List<Rule> provide() {
        return List.of();
    }
}
