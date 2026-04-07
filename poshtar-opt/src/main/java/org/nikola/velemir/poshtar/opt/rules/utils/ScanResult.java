package org.nikola.velemir.poshtar.opt.rules.utils;

import java.util.List;

 record ScanResult(boolean directCallFound, boolean throwFound, List<ForwardedCall> forwardedCalls) {

    public boolean hasExitPath() {
        return directCallFound || throwFound;
    }

    public boolean hasForwardedCalls() {
        return !forwardedCalls.isEmpty();
    }
}