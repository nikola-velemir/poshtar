package poshtar.tests;

import adapter.EnablePoshtar;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnablePoshtar
@EnableTransactionManagement
@SpringBootApplication
public class TestApplication {
}
