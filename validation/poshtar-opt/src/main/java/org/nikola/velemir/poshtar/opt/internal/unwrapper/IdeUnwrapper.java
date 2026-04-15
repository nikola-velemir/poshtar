package org.nikola.velemir.poshtar.opt.internal.unwrapper;

import org.nikola.velemir.poshtar.opt.internal.unwrapper.stategy.*;

import java.util.List;

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
