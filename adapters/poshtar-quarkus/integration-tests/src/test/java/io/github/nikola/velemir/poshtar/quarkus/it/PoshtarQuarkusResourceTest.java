package io.github.nikola.velemir.poshtar.quarkus.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class PoshtarQuarkusResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/poshtar-quarkus")
                .then()
                .statusCode(200)
                .body(is("Hello poshtar-quarkus"));
    }
}
