package io.github.nikola_velemir.poshtar.opt.internal.unwrapper.stategy;

import java.util.List;
/**
 * Provider that manages the registration and retrieval of IDE unwrapping strategies.
 * <p>
 * This class serves as an injector for the {@link UnwrapStrategy} implementations used by the
 * annotation processor.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class StrategyProvider {
    /**
     * Returns a list of available unwrapping strategies.
     *
     * @return A list of initialized {@link UnwrapStrategy} instances.
     */
    public static List<UnwrapStrategy> provideStrategies(){
        return List.of(
                new JetBrainsUnwrapStrategy(),
                new EclipseUnwrapStrategy(),
                new DefaultUnwrapStrategy()
        );
    }
}
