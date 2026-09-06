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

import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.HashSet;
import java.util.Set;

/**
 * Internal utility responsible for resolving method calls to their underlying source elements.
 * <p>
 * When the flow analyzer
 * encounters a method invocation, the {@code CalleeResolver}
 * attempts to locate the {@link ExecutableElement} of that method. This allows the
 * analyzer to "jump" into helper methods and verify if the pipeline is continued
 * elsewhere in the class.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class CalleeResolver {
    private final ProcessorContext ctx;
    private final ExecutableElement rootMethod;

    public CalleeResolver(ProcessorContext ctx, ExecutableElement method) {
        this.ctx = ctx;
        rootMethod = method;
    }

    /**
     * Attempts to resolve a method invocation to its corresponding executable element.
     * <p>
     * This method searches within the compilation unit paths of the current class.
     * To prevent infinite loops during recursion, it utilizes a set of already visited
     * elements.
     * </p>
     *
     * @param node    The method invocation node found in the AST.
     * @param visited A set of methods already processed in the current call stack.
     * @return The resolved {@link ExecutableElement}, or {@code null} if resolution failed.
     */
    public ExecutableElement resolveCallee(MethodInvocationTree node, Set<ExecutableElement> visited) {
        try {
            Trees trees = ctx.trees;
            for (TreePath path : getEnclosingPaths(visited)) {
                TreePath callPath = TreePath.getPath(path.getCompilationUnit(), node);
                if (callPath != null) {
                    Element el = trees.getElement(callPath);
                    if (el instanceof ExecutableElement) {
                        return (ExecutableElement) el;
                    }
                }
            }
        } catch (RuntimeException ignored) {
            // Resolution is best-effort — if it fails we skip recursion
        }
        return null;
    }

    private Iterable<TreePath> getEnclosingPaths(Set<ExecutableElement> visited) {
        Set<TreePath> paths = new HashSet<>();
        TreePath rootPath = ctx.trees.getPath(rootMethod);
        if (rootPath != null) paths.add(rootPath);
        for (ExecutableElement e : visited) {
            TreePath p = ctx.trees.getPath(e);
            if (p != null) paths.add(p);
        }
        return paths;
    }
}
