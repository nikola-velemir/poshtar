package demo.logs.repository;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import demo.logs.model.Log;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class LogRepositoryImpl implements LogRepository{
    private final Provider<EntityManager> em;

    @Transactional
    @Override
    public void save(Log log) {
        em.get().persist(log);

    }
}
