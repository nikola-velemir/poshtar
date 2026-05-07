/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.nikola_velemir.poshtar.opt.internal.unwrapper.stategy;

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
