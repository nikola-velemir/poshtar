package org.nikola.velemir.poshtar.opt.rules.deadPipeline.utils;

import com.sun.source.tree.MethodInvocationTree;

public record ForwardedCall( MethodInvocationTree node, int delegateArgIndex) {
}
