/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.validator.architecture.internal.rules.deadPipeline;

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