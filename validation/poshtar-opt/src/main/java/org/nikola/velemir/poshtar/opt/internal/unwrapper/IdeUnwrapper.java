package org.nikola.velemir.poshtar.opt.internal.unwrapper;

import org.nikola.velemir.poshtar.opt.internal.unwrapper.stategy.DefaultUnwrapStrategy;
import org.nikola.velemir.poshtar.opt.internal.unwrapper.stategy.EclipseUnwrapStrategy;
import org.nikola.velemir.poshtar.opt.internal.unwrapper.stategy.JetBrainsUnwrapStrategy;
import org.nikola.velemir.poshtar.opt.internal.unwrapper.stategy.UnwrapStrategy;

import java.util.List;

public class IdeUnwrapper {
    private static final List<UnwrapStrategy> STRATEGIES = List.of(
            new JetBrainsUnwrapStrategy(),
            new EclipseUnwrapStrategy(),
            new DefaultUnwrapStrategy()
    );

    public static <T> T unwrap(Class<? extends T> iface, T wrapper) {
        return STRATEGIES.stream()
                .filter(s -> s.supports(wrapper))
                .findFirst()
                .map(s -> s.unwrap(iface, wrapper))
                .orElse(wrapper);
    }
}
