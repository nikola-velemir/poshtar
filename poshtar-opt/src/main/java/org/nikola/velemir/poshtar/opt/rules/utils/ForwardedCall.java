package org.nikola.velemir.poshtar.opt.rules.utils;

import com.sun.source.tree.MethodInvocationTree;

 record ForwardedCall( MethodInvocationTree node, int delegateArgIndex) {
}
