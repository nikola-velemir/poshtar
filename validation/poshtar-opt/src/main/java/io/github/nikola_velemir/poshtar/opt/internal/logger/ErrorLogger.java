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

package io.github.nikola_velemir.poshtar.opt.internal.logger;

import javax.tools.Diagnostic;
/**
 * Error logger class that dispatches messages that will prevent project compilation (high severity).
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class ErrorLogger extends CoreLogger {

    public ErrorLogger() {
    }

    /**
     * Returns the diagnostic severity level for this logger implementation.
     *
     * @return Returns {@link javax.tools.Diagnostic.Kind} of ERROR.
     */
    @Override
    protected Diagnostic.Kind getKind() {
        return Diagnostic.Kind.ERROR;
    }

}
