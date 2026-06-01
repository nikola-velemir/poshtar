/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.validator.internal.unwrapper.stategy;

/**
 * Unwrapping strategy specifically designed for the Eclipse Integrated Development Environment.
 * <p>
 * This strategy uses reflection to access the underlying delegate field.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class EclipseUnwrapStrategy implements UnwrapStrategy {
    /**
     * Identifies if the object belongs to an Eclipse-prefixed package.
     *
     * @param wrapper The object to inspect.
     * @return {@code true} if the class name contains "eclipse"; {@code false} otherwise.
     */
    @Override
    public boolean supports(Object wrapper) {
        String name = wrapper.getClass().getName();
        return name.contains("eclipse") || name.contains("Eclipse");
    }

    /**
     * Attempts to extract the delegate environment from the Eclipse wrapper
     * using reflection on the {@code _processingEnv} field.
     *
     * @param <T>     The target type.
     * @param iface   The class literal of the target type.
     * @param wrapper The proxied object from the Eclipse environment.
     * @return The unwrapped delegate if successful; otherwise, returns the original wrapper.
     */
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
