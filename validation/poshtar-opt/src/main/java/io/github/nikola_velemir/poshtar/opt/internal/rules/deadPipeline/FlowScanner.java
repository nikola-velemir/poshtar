package io.github.nikola_velemir.poshtar.opt.internal.rules.deadPipeline;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.util.TreeScanner;

import java.util.ArrayList;
import java.util.List;

class FlowScanner extends TreeScanner<Void, Void> {
    private final String delegateName;

    private boolean directCallFound = false;
    private boolean throwFound = false;
    private final List<ForwardedCall> forwardedCalls = new ArrayList<>();

    public FlowScanner(String delegateName) {
        this.delegateName = delegateName;
    }

    @Override
    public Void visitMethodInvocation(MethodInvocationTree node, Void unused) {
        if (directCallFound) return null;

        String call = node.getMethodSelect().toString();

        if (call.equals(delegateName + ".handle") ||
                call.endsWith("." + delegateName + ".handle")) {
            directCallFound = true;
            return null;
        }
        List<? extends ExpressionTree> args = node.getArguments();
        for (int i = 0; i < args.size(); i++) {
            if (args.get(i).toString().equals(delegateName)) {
                forwardedCalls.add(new ForwardedCall(node, i));
                break;
            }
        }

        return super.visitMethodInvocation(node, unused);
    }

    @Override
    public Void visitThrow(ThrowTree node, Void unused) {
        throwFound = true;
        return null;
    }

    public ScanResult getResult() {
        return new ScanResult(directCallFound, throwFound, forwardedCalls);
    }
}