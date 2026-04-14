package org.nikola.velemir.poshtar.opt.internal.rules;


import java.util.List;

class RuleProvider {
    private static final List<Rule> rules = List.of(
            new BehaviourWiringRule(),
            new HandlerWiringRule(),
            new SingleResponsibilityHandlerRule(),
            new NoPrimitiveReturnTypesRule(),
            new OrphanRequestRule(),
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
