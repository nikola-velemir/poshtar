package io.github.nikola_velemir.poshtar.opt.internal.rules.deadPipeline;

import java.util.List;

/**
 * An immutable representation of the findings from a single-method AST scan.
 * <p>
 * This record acts as a summary of the potential execution paths within a method body.
 * It distinguishes between terminal paths (direct calls or exceptions) and
 * intermediate paths (where the pipeline delegate is passed to another method).
 * </p>
 *
 * @param directCallFound Indicates if a terminal {@code next.handle()} call was detected.
 * @param throwFound      Indicates if an exception is thrown, representing a valid intentional exit.
 * @param forwardedCalls  A list of invocations where the delegate was passed as an argument,
 *                        requiring further recursive analysis.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
record ScanResult(boolean directCallFound, boolean throwFound, List<ForwardedCall> forwardedCalls) {
    /**
     * Determines if the scanned method has a definitive local exit path.
     * <p>
     * A "local exit" is found if the code either completes the pipeline by calling the
     * next handler or terminates the request flow by throwing an exception.
     * </p>
     *
     * @return {@code true} if a direct call or a throw statement was found; {@code false} otherwise.
     */
    public boolean hasExitPath() {
        return directCallFound || throwFound;
    }
    /**
     * Checks if the pipeline delegate was forwarded into other method calls.
     *
     * @return {@code true} if there are method calls to investigate; {@code false} if the list is empty.
     */
    public boolean hasForwardedCalls() {
        return !forwardedCalls.isEmpty();
    }
}