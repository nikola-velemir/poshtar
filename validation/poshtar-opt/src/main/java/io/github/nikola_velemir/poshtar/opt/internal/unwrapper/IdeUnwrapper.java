package io.github.nikola_velemir.poshtar.opt.internal.unwrapper;

import io.github.nikola_velemir.poshtar.opt.internal.unwrapper.stategy.*;

public class IdeUnwrapper {

    public static <T> T unwrap(Class<? extends T> iface, T wrapper) {
        var strategies = StrategyProvider.provideStrategies();
        return strategies.stream()
                .filter(s -> s.supports(wrapper))
                .findFirst()
                .map(s -> s.unwrap(iface, wrapper))
                .orElse(wrapper);
    }
}
