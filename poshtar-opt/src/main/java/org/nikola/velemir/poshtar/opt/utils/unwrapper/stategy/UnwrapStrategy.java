package org.nikola.velemir.poshtar.opt.utils.unwrapper.stategy;

public interface UnwrapStrategy {
    boolean supports(Object wrapper);

    <T> T unwrap(Class<? extends T> iface, T wrapper);

}
