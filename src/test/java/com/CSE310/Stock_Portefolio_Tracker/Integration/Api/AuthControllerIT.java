package com.CSE310.Stock_Portefolio_Tracker.Integration.Api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.CSE310.Stock_Portefolio_Tracker.Dto.LoginRequestDto;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.notNullValue;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api/stockportefoliotracker/v1"; // ✅ IMPORTANT
    }

   


    @Test
    void RegisterUser() {
        given()
            .port(port)
            .contentType(ContentType.JSON)
            .body("""
                {
                  "username":"tix",
                  "email": "tox@gmail.com",
                  "password": "1x234560x"
                }
            """)
            .log().all()  // ✅ Log la requête
        .when()
            .post("/register")
           
        .then()
            .log().all()  // ✅ Log la réponse complète avec les détails de l'erreur
            .statusCode(201)
            .body("statusMsg", equalTo("USER CREATED SUCCESSFULLY"));
    }



    @Test
    void should_login_successfully() {

        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("yatte@gmail.com");
        request.setPassword("yatte");

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/login")
        .then()
            .log().all()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("token", notNullValue())
            .body("refresh", notNullValue());
}

    @Test
        void should_fail_login_with_invalid_credentials() {

            LoginRequestDto request = new LoginRequestDto();
            request.setUsername("yatte@gmail.com"); // utilisateur qui n'existe pas
            request.setPassword("yattex");           // mot de passe invalide

            given()
                .contentType(ContentType.JSON)
                .body(request)
                .log().all()
            .when()
                .post("/login")
            .then()
                .log().all()
                .statusCode(401) // on attend 401 pour login échoué
                .contentType(ContentType.JSON)
                .body("status", equalTo(401))
                .body("message", equalTo("UNAUTHORIZED"))
                .body("error", equalTo("BAD CREDENTIALS"));
        }
        

}
