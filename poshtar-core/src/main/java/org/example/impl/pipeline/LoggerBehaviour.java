package org.example.impl.pipeline;

import org.example.core.annotations.PipelineBehaviour;
import org.example.core.pipeline.behaviour.IVoidPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;

@PipelineBehaviour
public class LoggerBehaviour implements IVoidPipelineBehaviour {
    @Override
    public boolean supports(Class<?> requestType) {
        return true;
    }

    @Override
    public Object handle(Object o, RequestDelegate<Object> next) {
        System.out.println("LoggerBehaviour.handle");
        return next.handle();
    }
}
