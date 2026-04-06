package org.nikola.velemir.poshtar.opt.processor.utils.unwrapper.stategy;

public class DefaultUnwrapStrategy implements UnwrapStrategy {
    @Override
    public boolean supports(Object wrapper) {
        return true;
    }

    @Override
    public <T> T unwrap(Class<? extends T> iface, T wrapper) {
        return wrapper;
    }
}
