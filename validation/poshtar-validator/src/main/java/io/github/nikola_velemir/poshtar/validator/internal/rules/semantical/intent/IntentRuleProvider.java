package io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.intent;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleKind;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleProvider;

import java.util.List;
import java.util.stream.Stream;

public class IntentRuleProvider implements RuleProvider {
    @Override
    public RuleKind getKind() {
        return null;
    }

    @Override
    public List<Rule> provide() {
        return Stream.of(
                        new RequestHandlerIntentRuleProvider().provide(),
                        new RequestIntentRuleProvider().provide()
                ).flatMap(List::stream)
                .toList();
    }
}
