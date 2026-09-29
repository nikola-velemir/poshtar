package io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.intent.handler.hierarchy;

import io.github.nikola_velemir.poshtar.core.request.Command;
import io.github.nikola_velemir.poshtar.core.request.Query;
import io.github.nikola_velemir.poshtar.core.request.handler.CommandHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.QueryHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

class RequestHandlerHierarchyRule implements Rule {
    private static final String REQUEST_HANDLER_FQN = RequestHandler.class.getName();
    private static final String QUERY_FQN = Query.class.getName();
    private static final String COMMAND_FQN = Command.class.getName();
    private static final String VIOLATION_MESSAGE = String.format(
            "must not extend or implement %s directly. Use %s or %s.",
            RequestHandler.class.getName(),
            QueryHandler.class.getName(),
            CommandHandler.class.getName()
    );
    private static final Logger logger = LoggerProvider.provideWarningLogger();

    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        var elements = ctx.getElements();
        var types = ctx.getTypes();

        TypeElement requestHandlerElement = elements.getTypeElement(REQUEST_HANDLER_FQN);
        if (requestHandlerElement == null) return;

        TypeMirror erasedRequestHandler = types.erasure(requestHandlerElement.asType());

        for (Element root : roundEnv.getRootElements()) {
            check(root, erasedRequestHandler, ctx);
        }
    }

    private void check(Element element, TypeMirror erasedRequestHandler, ProcessorContext ctx) {
        if (!(element instanceof TypeElement type)) return;

        var types = ctx.getTypes();

        for (TypeMirror iface : type.getInterfaces()) { // direct only; covers class implements and interface extends
            if (!types.isSameType(types.erasure(iface), erasedRequestHandler)) continue;

            var targetName = type.getQualifiedName();
            String message = String.format("%s %s", targetName, VIOLATION_MESSAGE);
            logger.log(ctx.env, message, element);
        }

        for (TypeElement nested : ElementFilter.typesIn(type.getEnclosedElements())) {
            check(nested, erasedRequestHandler, ctx);
        }
    }
}