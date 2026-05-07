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

package io.github.nikola_velemir.poshtar.opt.internal.registry.scanner;

/**
 * Class acts a "injector", that will provide {@link RegistryScanner} implementation,
 * as its concrete implementation is package-private.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class RegistryScannerProvider {
    /**
     * Provides an implementation of {@link RegistryScanner}, used for class path scanning.
     *
     * @return Concrete implementation of {@link RegistryScanner}
     */
    public static RegistryScanner provideScanner() {
        return new RegistryScannerImpl();
    }
}
