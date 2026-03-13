package poshtar.tests;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.PlatformTransactionManager;

@TestConfiguration
public class MockTransactionConfig {
    @Bean
    public org.springframework.jdbc.datasource.DriverManagerDataSource dataSource() {
        org.springframework.jdbc.datasource.DriverManagerDataSource ds = new org.springframework.jdbc.datasource.DriverManagerDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager(javax.sql.DataSource dataSource) {
        return new org.springframework.jdbc.support.JdbcTransactionManager(dataSource);
    }
}