package org.example.core.validation;

import org.example.core.annotations.RequestHandler;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.*;

@SupportedAnnotationTypes("org.example.types.annotations.RequestHandler")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class RequestHandlerProcessor extends AbstractProcessor {

    private final Map<String, String> requestToHandler = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element element : roundEnv.getElementsAnnotatedWith(RequestHandler.class)) {
            if (element.getKind() != ElementKind.CLASS) continue;

            TypeElement handlerClass = (TypeElement) element;

            TypeMirror requestType = null;
            for (TypeMirror iface : handlerClass.getInterfaces()) {
                String ifaceStr = iface.toString();
                if (ifaceStr.startsWith("org.example.types.requestHandler.IRequestHandler<")) {
                    String inside = ifaceStr.substring(ifaceStr.indexOf("<") + 1, ifaceStr.lastIndexOf(","));
                    requestType = processingEnv.getElementUtils()
                            .getTypeElement(inside).asType();
                    break;
                }
            }

            if (requestType == null) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Cannot determine request type for handler " + handlerClass.getSimpleName(),
                        element);
                continue;
            }

            String requestName = requestType.toString();
            if (requestToHandler.containsKey(requestName)) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Multiple handlers found for request: " + requestName
                                + ". Already handled by " + requestToHandler.get(requestName),
                        element);
            } else {
                requestToHandler.put(requestName, handlerClass.getQualifiedName().toString());
            }
        }

        return true;
    }
}
