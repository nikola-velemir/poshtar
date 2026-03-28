package org.nikola.velemir.poshtar.opt.rules.deadPipeline.utils;

import java.util.List;

public record ScanResult(boolean directCallFound, boolean throwFound, List<ForwardedCall> forwardedCalls) {

    public boolean hasExitPath() {
        return directCallFound || throwFound;
    }

    public boolean hasForwardedCalls() {
        return !forwardedCalls.isEmpty();
    }
}