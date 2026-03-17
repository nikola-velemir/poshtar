package poshtar.tests;

import nikola.velemir.poshtar.spring.adapter.configuration.EnablePoshtar;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnablePoshtar
@EnableTransactionManagement
@SpringBootApplication
public class TestApplication {
}
