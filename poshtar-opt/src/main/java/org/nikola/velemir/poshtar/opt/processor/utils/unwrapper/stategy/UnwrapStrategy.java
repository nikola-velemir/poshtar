package org.nikola.velemir.poshtar.opt.processor.utils.unwrapper.stategy;

public interface UnwrapStrategy {
    boolean supports(Object wrapper);

    <T> T unwrap(Class<? extends T> iface, T wrapper);

}
