package demo.pipeline;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import demo.logs.model.Log;
import demo.logs.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@Behaviour
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class LoggingBehaviour<TRequest extends Request<TResponse>, TResponse>
        implements PipelineBehaviour<TRequest, TResponse> {

    private static final Logger logger = LoggerFactory.getLogger(LoggingBehaviour.class);
    private final LogRepository repository;

    @Override
    @Transactional
    public TResponse handle(TRequest request, RequestDelegate<TRequest, TResponse> next) {
        String requestName = request.getClass().getSimpleName();

        logger.info("--- [PoshtaR] Pre-processing: {} ---", requestName);
        long startTime = System.currentTimeMillis();

        try {
            TResponse response = next.handle(request);

            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("--- [PoshtaR] Post-processing: {} (Uspelo za {}ms) ---", requestName, executionTime);

            repository.save(new Log(Instant.now(), "LOG"));
            return response;

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("--- [PoshtaR] Error u {}: {} (Puklo nakon {}ms) ---",
                    requestName, e.getMessage(), executionTime);
            repository.save(new Log(Instant.now(), "LOG"));

            throw e;
        }
    }
}