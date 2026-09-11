package io.github.nikola_velemir.poshtar.micronaut.request;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.transaction.SynchronousTransactionManager;
import io.micronaut.transaction.TransactionCallback;
import io.micronaut.transaction.TransactionDefinition;
import io.micronaut.transaction.TransactionStatus;
import io.micronaut.transaction.exceptions.NoTransactionException;
import jakarta.inject.Singleton;

import java.util.Optional;

import static org.mockito.Mockito.mock;

@Factory
@SuppressWarnings("unchecked")
public class MockTransactionConfig {

    @Singleton
    @Primary
    @Replaces(SynchronousTransactionManager.class)
    public SynchronousTransactionManager<Object> mockTransactionManager() {
        return new SynchronousTransactionManager<>() {

            @Override
            public Object getConnection() {
                return null;
            }

            @Override
            public boolean hasConnection() {
                return false;
            }

            @Override
            public Optional<? extends TransactionStatus<?>> findTransactionStatus() {
                return Optional.empty();
            }

            @Override
            public <T> T execute(TransactionDefinition definition, TransactionCallback<Object, T> callback) {
                // Automatically handle the MANDATORY check for your test
                if (definition.getPropagationBehavior() == TransactionDefinition.Propagation.MANDATORY) {
                    throw new NoTransactionException("No existing transaction found for transaction marked with propagation 'mandatory'");
                }
                // Use .apply() instead of .doInTransaction() or .call()
                return (T) callback.apply(mock(TransactionStatus.class));
            }

            @Override
            public <T> T executeRead(TransactionCallback<Object, T> callback) {
                return (T) callback.apply(mock(TransactionStatus.class));
            }

            @Override
            public <T> T executeWrite(TransactionCallback<Object, T> callback) {
                return (T) callback.apply(mock(TransactionStatus.class));
            }

            @Override
            public TransactionStatus<Object> getTransaction(TransactionDefinition definition) {
                return mock(TransactionStatus.class);
            }

            @Override
            public void commit(TransactionStatus<Object> status) {
                // No-op
            }

            @Override
            public void rollback(TransactionStatus<Object> status) {
                // No-op
            }
        };
    }
}