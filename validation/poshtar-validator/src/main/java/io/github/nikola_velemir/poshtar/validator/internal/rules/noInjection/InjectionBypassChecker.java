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

package io.github.nikola_velemir.poshtar.validator.internal.rules.noInjection;

import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

class InjectionBypassChecker {
    private static final String BYPASS_ANNOTATION_FQN = OverruleNoInjection.class.getName();

    public static boolean isBypassed(TypeElement clazz, ProcessorContext ctx) {

        return hasAnnotation(clazz, ctx) && isInTestPackage(clazz, ctx);
    }

    private static boolean hasAnnotation(TypeElement clazz, ProcessorContext ctx) {
        return clazz.getAnnotationMirrors().stream().map(mirror -> mirror.getAnnotationType().asElement().toString()).anyMatch(BYPASS_ANNOTATION_FQN::equals);
    }

    private static boolean isInTestPackage(TypeElement clazz, ProcessorContext ctx) {
        boolean hasTestAnnotations = clazz.getAnnotationMirrors().stream()
                .map(m -> m.getAnnotationType().asElement().toString())
                .anyMatch(fqn -> fqn.startsWith("org.junit.") || fqn.startsWith("org.testng."));
        boolean isInTestFiles = checkTestFiles(clazz, ctx);
        return hasTestAnnotations || isInTestFiles;
    }

    private static boolean checkTestFiles(TypeElement clazz, ProcessorContext ctx) {
        String fqn = clazz.getQualifiedName().toString();
        try {
            FileObject resource = ctx.env.getFiler()
                    .getResource(StandardLocation.SOURCE_PATH,
                            ctx.env.getElementUtils()
                                    .getPackageOf(clazz).getQualifiedName().toString(),
                            clazz.getSimpleName() + ".java");
            String uri = resource.toUri().toString();
            if (uri.contains("/test/")) return true;
        } catch (IOException | IllegalArgumentException ignored) {
        }

        String sourceRoots = ctx.env.getOptions().get("sourceRoots");
        if (sourceRoots != null) {
            String classPath = fqn.replace('.', '/') + ".java";
            return Arrays.stream(sourceRoots.split(File.pathSeparator))
                    .anyMatch(root -> checkTargetPath(root, classPath));
        }

        return false;
    }

    private static boolean checkTargetPath(String root, String classPath) {
        try {
            Path baseDir = Paths.get(root).toAbsolutePath().normalize();
            Path targetFile = baseDir.resolve(classPath).toAbsolutePath().normalize();

            if (!targetFile.startsWith(baseDir)) {
                return false;
            }

            return Files.exists(targetFile) && baseDir.toString().replace('\\', '/').contains("/test/");
        } catch (Exception e) {
            return false;
        }
    }
}
