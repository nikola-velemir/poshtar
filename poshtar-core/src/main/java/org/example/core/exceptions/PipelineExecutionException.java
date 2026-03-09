package org.example.core.exceptions;

public class PipelineExecutionException extends PoshtarException {
    public PipelineExecutionException(String behaviourName, Throwable cause) {
        super("Failure in execution of the pipeline behaviour: [" + behaviourName + "]", cause);
    }
}