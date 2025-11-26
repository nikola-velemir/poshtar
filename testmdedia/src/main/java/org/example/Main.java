package org.example;

import org.example.impl.*;
import org.example.impl.notification.BasicNotification;
import org.example.impl.notification.BasicNotificationHandler;
import org.example.impl.notification.NotificationRegistry;
import org.example.impl.notification.OtherNotificationHandler;
import org.example.impl.pipeline.BasicRequestPipeline;
import org.example.impl.pipeline.LoggerBehaviour;
import org.example.impl.pipeline.OtherRequestPipeline;
import org.example.impl.pipeline.PipelineRegistry;
import org.example.impl.request.*;
import org.example.core.mediator.IMediator;
import org.example.core.request.registry.IHandlerRegistry;
import org.example.core.notification.registry.INotificationRegistry;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        IHandlerRegistry handlerRegistry = new HandlerRegistry();
        INotificationRegistry notificationRegistry = new NotificationRegistry();
        var request = new BasicRequest();
        var voidRequest = new OtherRequest();
        var basicRequestHandler = new BasicRequestHandler();
        var otherRequestHandler = new OtherBasicHandler();

        handlerRegistry.register(BasicRequest.class, basicRequestHandler);
        handlerRegistry.register(OtherRequest.class, otherRequestHandler);

        var notification = new BasicNotification();

        var notificationHandler1 = new BasicNotificationHandler();
        var notificationHandler2 = new OtherNotificationHandler();
        notificationRegistry.register(BasicNotification.class, notificationHandler1);
        notificationRegistry.register(BasicNotification.class, notificationHandler2);

        var pipelineRegistry = new PipelineRegistry();
        var loggerBehaviour = new LoggerBehaviour();
        var basicRequestPipeline = new BasicRequestPipeline();
        var otherRequestPipeline = new OtherRequestPipeline();
        pipelineRegistry.register(loggerBehaviour);
        pipelineRegistry.register(basicRequestPipeline);
        pipelineRegistry.register(otherRequestPipeline);

        IMediator mediator = new Mediator(handlerRegistry,notificationRegistry,pipelineRegistry);
        var response = mediator.send(request);
        System.out.println(response.content());

        var voidResponse = mediator.send(voidRequest);
        System.out.println(voidResponse);

        mediator.publish(notification);



    }
}
