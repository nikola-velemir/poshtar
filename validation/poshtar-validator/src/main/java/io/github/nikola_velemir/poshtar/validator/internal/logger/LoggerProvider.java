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

package io.github.nikola_velemir.poshtar.validator.internal.logger;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;

/**
 * Provider that manages the instantiation and retrieval of {@link Logger} implementations.
 * <p>
 * This class serves as an injector for the {@link Logger} implementations used by the
 * {@link Rule} implementations.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class LoggerProvider {
    private static final ErrorLogger errorLogger = new ErrorLogger();
    private static final WarningLogger warningLogger = new WarningLogger();

    /**
     * Provides an instance of {@link ErrorLogger}, used by rule classes.
     *
     * @return Ready instance of {@link  ErrorLogger}
     */
    public static Logger provideErrorLogger() {
        return errorLogger;
    }

    /**
     * Provides an instance of {@link WarningLogger}, used by rule classes.
     *
     * @return Ready instance of {@link  WarningLogger}
     */
    public static Logger provideWarningLogger() {
        return warningLogger;
    }

}
