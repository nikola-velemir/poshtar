package io.github.nikola_velemir.poshtar.opt.internal.rules.deadPipeline;

import com.sun.source.tree.MethodInvocationTree;

/**
 * Record models forwarded call metadata, used by a {@link FlowAnalyser}.
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
record ForwardedCall(MethodInvocationTree node, int delegateArgIndex) {
}
