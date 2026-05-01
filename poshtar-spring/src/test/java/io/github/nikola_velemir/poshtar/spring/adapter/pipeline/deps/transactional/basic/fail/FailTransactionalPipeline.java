package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.fail;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import io.github.nikola_velemir.poshtar.spring.adapter.model.TestEntity;
import io.github.nikola_velemir.poshtar.spring.adapter.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Behaviour
public class FailTransactionalPipeline implements PipelineBehaviour<FailTransactionalRequest, Unit> {

    @Autowired
    private final TestRepository testRepository;

    public FailTransactionalPipeline(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    @Transactional
    public Unit handle(FailTransactionalRequest request, RequestDelegate<FailTransactionalRequest, Unit> delegate) {
        var te = new TestEntity(request.payload());
        testRepository.save(te);
        throw new RuntimeException("Failing on purpose");
    }
}
