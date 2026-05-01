package io.github.nikola_velemir.poshtar.opt.internal.rules.deadPipeline;

import com.sun.source.tree.MethodInvocationTree;

record ForwardedCall(MethodInvocationTree node, int delegateArgIndex) {
}
