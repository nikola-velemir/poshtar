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

package io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.deadPipeline;

import com.sun.source.tree.MethodTree;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import javax.lang.model.element.ExecutableElement;
import java.util.HashSet;
import java.util.Set;

/**
 * Depth-first flow analysis within a {@code Behaviour} implementation.
 * <p>
 * This class determines if a pipeline's execution path is "safe" by verifying that
 * every logical branch eventually leads to a terminal operation: either a call to
 * {@code handle()} on the next delegate or an explicit exception.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class FlowAnalyser {
    private final ProcessorContext ctx;
    private final CalleeResolver resolver;
    private final Set<ExecutableElement> visited = new HashSet<>();

    /**
     * Instantiates a new {@link FlowAnalyser},
     * with the current processor context and root method to begin analysis.
     *
     * @param ctx        Processor context, containing all found FQNs of requests, handlers and behaviors.
     * @param rootMethod Method where the flow analysis begins.
     */
    public FlowAnalyser(ProcessorContext ctx, ExecutableElement rootMethod) {
        this.ctx = ctx;
        this.resolver = new CalleeResolver(ctx, rootMethod);
    }

    /**
     * Performs a recursive analysis of the provided method body.
     *
     * @param methodTree   The AST representation of the method to scan.
     * @param delegateName The current identifier name of the {@code RequestDelegate}.
     * @return {@code true} if a valid exit path is found; {@code false} otherwise.
     */
    public boolean analyse(MethodTree methodTree, String delegateName) {
        FlowScanner scanner = new FlowScanner(delegateName);
        scanner.scan(methodTree.getBody(), null);
        ScanResult result = scanner.getResult();
        if (result.hasExitPath()) return true;
        if (result.hasForwardedCalls()) {
            for (var forwarded : result.forwardedCalls()) {
                if (recurseInto(forwarded)) return true;
            }
        }
        return false;
    }

    private boolean recurseInto(ForwardedCall forwarded) {
        ExecutableElement callee = resolver.resolveCallee(forwarded.node(), visited);
        if (callee == null) return false;
        if (!visited.add(callee)) return false;

        if (forwarded.delegateArgIndex() >= callee.getParameters().size()) return false;

        String remappedName = callee.getParameters()
                .get(forwarded.delegateArgIndex())
                .getSimpleName()
                .toString();

        MethodTree calleeTree = ctx.trees.getTree(callee);
        if (calleeTree == null) return false;

        return analyse(calleeTree, remappedName);
    }
}
