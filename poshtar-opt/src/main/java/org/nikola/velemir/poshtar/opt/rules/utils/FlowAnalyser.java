package org.nikola.velemir.poshtar.opt.rules.utils;

import com.sun.source.tree.MethodTree;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.lang.model.element.ExecutableElement;
import java.util.HashSet;
import java.util.Set;

 public class FlowAnalyser {
    private final RuleContext ctx;
    private final CalleeResolver resolver;
    private final Set<ExecutableElement> visited = new HashSet<>();

    public FlowAnalyser(RuleContext ctx, ExecutableElement rootMethod) {
        this.ctx = ctx;
        this.resolver = new CalleeResolver(ctx, rootMethod);
    }

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
