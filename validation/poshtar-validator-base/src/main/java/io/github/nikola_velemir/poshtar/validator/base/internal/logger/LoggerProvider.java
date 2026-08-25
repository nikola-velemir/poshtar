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

package io.github.nikola_velemir.poshtar.validator.base.internal.logger;


import io.github.nikola_velemir.poshtar.validator.base.internal.rules.Rule;

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
