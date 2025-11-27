package org.example.impl;

import org.example.core.mediator.IMediator;
import org.example.core.pipeline.IPipelineRegistry;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.request.registry.IHandlerRegistry;
import org.example.core.notification.registry.INotificationRegistry;
import org.example.core.notification.INotification;
import org.example.core.request.IRequest;

import java.util.List;
import java.util.function.Supplier;

public class Mediator implements IMediator {
    private final IHandlerRegistry requestRegistry;
    private final INotificationRegistry notificationRegistry;
    private final IPipelineRegistry pipelineRegistry;

    public Mediator(IHandlerRegistry registry, INotificationRegistry notificationRegistry,IPipelineRegistry pipelineRegistry) {
        this.requestRegistry = registry;
        this.notificationRegistry = notificationRegistry;
        this.pipelineRegistry = pipelineRegistry;
    }
    @Override
    public <TRequest extends IRequest<TResponse>, TResponse> TResponse send(TRequest tRequest) {


        var handler = requestRegistry.resolve((Class<TRequest>) tRequest.getClass());

        Supplier<TResponse> handlerInvoker = () -> handler.handle(tRequest);
        List<IPipelineBehaviour<TRequest,TResponse>> behaviours =
                pipelineRegistry.resolve((Class<TRequest>) tRequest.getClass());

        return invokePipeLine(tRequest, handlerInvoker, behaviours);
    }
    private <TRequest extends IRequest<TResponse>,TResponse> TResponse invokePipeLine(
            TRequest request,
            Supplier<TResponse> handlerInvoker,
            List<IPipelineBehaviour<TRequest,TResponse>> behaviours
    ){
        RequestDelegate<TResponse> next = handlerInvoker::get;
        for(int i = behaviours.size()-1; i >= 0; i--){
            IPipelineBehaviour<TRequest,TResponse> behaviour = behaviours.get(i);
            RequestDelegate<TResponse> currentNext = next;
            next = () -> behaviour.handle(request,currentNext);
        }
        return next.handle();
    }
    @Override
    public <TNotification extends INotification> void publish(TNotification notification) {
        var handlers = notificationRegistry.resolve((Class<TNotification>) notification.getClass());
        for(var handler: handlers) {
            handler.handle(notification);
        }
    }
}
