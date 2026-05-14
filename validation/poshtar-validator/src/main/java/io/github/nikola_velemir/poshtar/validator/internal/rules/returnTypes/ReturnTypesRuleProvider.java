package io.github.nikola_velemir.poshtar.validator.internal.rules.returnTypes;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;

import java.util.List;

public class ReturnTypesRuleProvider {
   public static List<Rule> provide(){
        return List.of(
                new NoPrimitiveReturnTypesRule()
        );
    }
}
