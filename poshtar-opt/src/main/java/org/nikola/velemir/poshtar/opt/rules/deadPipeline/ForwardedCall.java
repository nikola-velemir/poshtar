package org.nikola.velemir.poshtar.opt.rules.deadPipeline;

import com.sun.source.tree.MethodInvocationTree;

record ForwardedCall(MethodInvocationTree node, int delegateArgIndex) {
}
