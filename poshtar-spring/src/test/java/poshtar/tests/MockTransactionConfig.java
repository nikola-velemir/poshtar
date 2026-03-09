package poshtar.tests;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.PlatformTransactionManager;

@TestConfiguration
public class MockTransactionConfig {
    @Bean
    public PlatformTransactionManager transactionManager() {
        PlatformTransactionManager tm = org.mockito.Mockito.mock(PlatformTransactionManager.class);

        org.mockito.Mockito.when(tm.getTransaction(org.mockito.ArgumentMatchers.any())).thenAnswer(invocation -> {
            org.springframework.transaction.TransactionDefinition definition = invocation.getArgument(0);
            if (definition.getPropagationBehavior() == org.springframework.transaction.TransactionDefinition.PROPAGATION_MANDATORY) {
                throw new IllegalTransactionStateException("No existing transaction found for transaction marked with propagation 'mandatory'");
            }
            return new org.springframework.transaction.support.SimpleTransactionStatus();
        });

        return tm;
    }
}