package io.github.nikola_velemir.poshtar.opt.internal.unwrapper.stategy;

import java.util.List;

public class StrategyProvider {
    public static List<UnwrapStrategy> provideStrategies(){
        return List.of(
                new JetBrainsUnwrapStrategy(),
                new EclipseUnwrapStrategy(),
                new DefaultUnwrapStrategy()
        );
    }
}
