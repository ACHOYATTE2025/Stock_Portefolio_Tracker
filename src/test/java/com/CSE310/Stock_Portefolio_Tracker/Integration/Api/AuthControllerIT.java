package com.CSE310.Stock_Portefolio_Tracker.Integration.Api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


import io.restassured.http.ContentType;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/stockdb",
    "spring.datasource.username=postgres",
    "spring.datasource.password=Dreamcast1985@.@",
    "jwt.key=qsd4qs86fqs54c8qs94d856qs4d8/s4d56qs1d89qs4dx56qs1d86qs4"
})
public class AuthControllerIT {
    @LocalServerPort
    int port;


    @Test
    void RegisterUser() {
        given()
            .port(port)
            .contentType(ContentType.JSON)
            .body("""
                {
                  "username":"acho",
                  "email": "test@test.com",
                  "password": "123456"
                }
            """)
            .log().all()  // ✅ Log la requête
        .when()
            .post("/api/stockportefoliotracker/v1/registerUser")
           
        .then()
            .log().all()  // ✅ Log la réponse complète avec les détails de l'erreur
            .statusCode(201)
            .body("statusMsg", equalTo("USER CREATED SUCCESSFULLY"));
    }

}
