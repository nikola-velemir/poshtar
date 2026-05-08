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

package io.github.nikola_velemir.poshtar.validator.internal.unwrapper.stategy;
/**
 * Unwrapping strategy specifically designed for the JetBrains (IntelliJ IDEA) build environment.
 * <p>
 * This strategy detects the
 * presence of IntelliJ's {@code APIWrappers} utility and uses reflection to extract the
 * underlying compiler implementation.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class JetBrainsUnwrapStrategy implements UnwrapStrategy {
    private static final String API_WRAPPERS = "org.jetbrains.jps.javac.APIWrappers";
    /**
     * Checks if the JetBrains JPS API wrappers are available in the current class loader.
     *
     * @param wrapper The object to check.
     * @return {@code true} if the IntelliJ wrapper API is detected; {@code false} otherwise.
     */
    @Override
    public boolean supports(Object wrapper) {
        try {
            wrapper.getClass().getClassLoader().loadClass(API_WRAPPERS);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
    /**
     * Invokes the IntelliJ {@code APIWrappers.unwrap} method via reflection to recover
     * the raw compiler component.
     *
     * @param <T>     The target type.
     * @param iface   The class literal of the target type.
     * @param wrapper The proxied object from the JetBrains environment.
     * @return The unwrapped object if successful; otherwise, returns the original wrapper.
     */
    @Override
    public <T> T unwrap(Class<? extends T> iface, T wrapper) {
        try {

            final Class<?> apiWrappers = wrapper.getClass().getClassLoader()
                    .loadClass("org.jetbrains.jps.javac.APIWrappers");
            final java.lang.reflect.Method unwrapMethod = apiWrappers
                    .getDeclaredMethod("unwrap", Class.class, Object.class);
            return iface.cast(unwrapMethod.invoke(null, iface, wrapper));
        } catch (Throwable ignored) {
            return wrapper;
        }
    }
}
