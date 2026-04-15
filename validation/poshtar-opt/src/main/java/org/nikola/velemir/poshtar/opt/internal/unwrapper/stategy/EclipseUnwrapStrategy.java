package org.nikola.velemir.poshtar.opt.internal.unwrapper.stategy;

class EclipseUnwrapStrategy implements UnwrapStrategy {
    @Override
    public boolean supports(Object wrapper) {
        String name = wrapper.getClass().getName();
        return name.contains("eclipse") || name.contains("Eclipse");
    }

    @Override
    public <T> T unwrap(Class<? extends T> iface, T wrapper) {
        try {
            java.lang.reflect.Field delegate = wrapper.getClass()
                    .getDeclaredField("_processingEnv");
            delegate.setAccessible(true);
            Object unwrapped = delegate.get(wrapper);
            if (iface.isInstance(unwrapped)) return iface.cast(unwrapped);
        } catch (Throwable ignored) {
        }
        return wrapper;
    }
}
