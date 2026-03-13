package com.example.demo.pipeline;

import org.example.core.annotations.PipelineBehaviour;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.request.IRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

@PipelineBehaviour
@Order(1)
public class LoggingBehaviour<TRequest extends IRequest<TResponse>, TResponse>
        implements IPipelineBehaviour<TRequest, TResponse> {

    private static final Logger logger = LoggerFactory.getLogger(LoggingBehaviour.class);

    @Override
    public TResponse handle(TRequest request, RequestDelegate<TRequest, TResponse> next) {
        String requestName = request.getClass().getSimpleName();

        logger.info("--- [PoshtaR] Pre-processing: {} ---", requestName);
        long startTime = System.currentTimeMillis();

        try {
            TResponse response = next.handle(request);

            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("--- [PoshtaR] Post-processing: {} (Uspelo za {}ms) ---", requestName, executionTime);

            return response;

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("--- [PoshtaR] Error u {}: {} (Puklo nakon {}ms) ---",
                    requestName, e.getMessage(), executionTime);
            throw e;
        }
}

//    @Override
//    public TResponse handle(TRequest request, RequestDelegate<TResponse> next) {
//        String requestName = request.getClass().getSimpleName();
//
//        logger.info("--- [PoshtaR] Pre-processing: {} ---", requestName);
//        long startTime = System.currentTimeMillis();
//
//        try {
//            TResponse response = next.handle();
//
//            long executionTime = System.currentTimeMillis() - startTime;
//            logger.info("--- [PoshtaR] Post-processing: {} (Uspelo za {}ms) ---", requestName, executionTime);
//
//            return response;
//
//        } catch (Exception e) {
//            long executionTime = System.currentTimeMillis() - startTime;
//            logger.error("--- [PoshtaR] Error u {}: {} (Puklo nakon {}ms) ---",
//                    requestName, e.getMessage(), executionTime);
//            throw e;
//        }
//    }
}