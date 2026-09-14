package io.github.nikola_velemir.poshtar.micronaut.it;
import io.micronaut.runtime.Micronaut; // Missing import

public class TestApplication {
    public static void main(String[] args) {
        Micronaut.run(TestApplication.class, args);
    }
}