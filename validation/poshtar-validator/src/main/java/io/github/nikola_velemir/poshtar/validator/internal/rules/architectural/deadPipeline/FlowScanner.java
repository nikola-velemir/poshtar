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

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.util.TreeScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * A specialized AST visitor, identifies pipeline exit points and delegation patterns.
 * <p>
 * This scanner traverses the method body to find occurrences of the {@code RequestDelegate}
 * (the "next" link). It categorizes findings into three categories:
 * <ol>
 *     <li><b>Direct Calls:</b> Explicit invocations of {@code delegate.handle()}.</li>
 *     <li><b>Throws:</b> Exception handling that intentionally halts the pipeline.</li>
 *     <li><b>Forwarded Calls:</b> Passing the delegate as an argument to another method.</li>
 * </ol>
 * </p>
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class FlowScanner extends TreeScanner<Void, Void> {
    private final String delegateName;

    private boolean directCallFound = false;
    private boolean throwFound = false;
    private final List<ForwardedCall> forwardedCalls = new ArrayList<>();

    public FlowScanner(String delegateName) {
        this.delegateName = delegateName;
    }

    /**
     * Inspects method invocations to check for pipeline forwarding.
     * <p>
     * If the method being called is {@code handle} on the target delegate, it is
     * marked as a direct exit. If the delegate is passed as an argument,
     * it is recorded for recursive analysis.
     * </p>
     */
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

    /**
     * Marks the current path as terminated if a {@code throw} statement is encountered.
     */
    @Override
    public Void visitThrow(ThrowTree node, Void unused) {
        throwFound = true;
        return null;
    }

    /**
     * Aggregates the scanning findings into an immutable result container.
     *
     * @return A {@link ScanResult} reflecting the state of the scanned AST.
     */
    public ScanResult getResult() {
        return new ScanResult(directCallFound, throwFound, forwardedCalls);
    }
}