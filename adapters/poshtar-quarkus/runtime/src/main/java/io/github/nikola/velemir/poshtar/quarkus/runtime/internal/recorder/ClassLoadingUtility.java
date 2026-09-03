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

package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.recorder;

/**
 * Class loading utility, used to resolve class FQNs.
 *
 * @author Nikola Velemir
 * @since 1.0.0
 */
final class ClassLoadingUtility {
    private ClassLoadingUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Loads a class by its fully qualified name using the current thread's context class loader.
     *
     * @param name the fully qualified name of the desired class
     * @return the resolved {@link Class} object
     * @throws RuntimeException if the class cannot be located by the class loader
     */
    public static Class<?> loadClass(String name) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            return Class.forName(name, true, cl);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load class: " + name, e);
        }
    }
}
