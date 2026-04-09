package org.nikola.velemir.poshtar.opt.rules;


import java.util.List;

public class RuleProvider {
    private static final List<Rule> rules = List.of(
            new SingleResponsibilityHandlerRule(),
            new NoPrimitiveReturnTypesRule(),
            new UnregisteredRequestRule(),
            new RequestFinalityRule(),
            new AmbiguityRule(),
            new HandlerNoInjectionRule(),
            new BehaviourNoInjectionRule(),
            new DeadPipelineRule()
    );

    public static List<Rule> provideRules() {
        return rules;
    }
}
