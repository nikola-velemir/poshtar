package org.nikola.velemir.poshtar.opt.internal.rules.deadPipeline;

import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import org.nikola.velemir.poshtar.opt.processor.ProcessorContext;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.HashSet;
import java.util.Set;

class CalleeResolver {
    private final ProcessorContext ctx;
    private final ExecutableElement rootMethod;

    public CalleeResolver(ProcessorContext ctx, ExecutableElement method) {
        this.ctx = ctx;
        rootMethod = method;
    }

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
