package io.github.nikola_velemir.poshtar.opt.internal.rules;


import java.util.List;

/**
 * Class that provides all architectural rules to be checked during compilation.
 *
 * <p>Class is acts as "injector", providing rules to validator classes</p>
 */
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

    /**
     * Provides all architectural rules to validate.
     *
     * @return List of rules to be validated.
     */
    public static List<Rule> provideRules() {
        return rules;
    }
}
