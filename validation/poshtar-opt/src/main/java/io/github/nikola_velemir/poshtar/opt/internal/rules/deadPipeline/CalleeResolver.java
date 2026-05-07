/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.nikola_velemir.poshtar.opt.internal.rules.deadPipeline;

import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;

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
        } catch (Exception ignored) {
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
