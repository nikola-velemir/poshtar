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
 * Unwrapping strategy that is defaulted to if no previous strategies score a match.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class DefaultUnwrapStrategy implements UnwrapStrategy {
    @Override
    public boolean supports(Object wrapper) {
        return true;
    }

    @Override
    public <T> T unwrap(Class<? extends T> iface, T wrapper) {
        return wrapper;
    }
}
