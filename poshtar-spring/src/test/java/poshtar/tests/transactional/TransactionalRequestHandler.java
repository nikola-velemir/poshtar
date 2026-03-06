package poshtar.tests.transactional;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.springframework.transaction.annotation.Transactional;
@RequestHandler
public class TransactionalRequestHandler implements IRequestHandler<TransactionalRequest, String> {

    public TransactionalRequestHandler() {
    }

    @Transactional
    @Override
    public String handle(TransactionalRequest injectionRequest) {
        return "Request with " + injectionRequest.payload();
    }
}
