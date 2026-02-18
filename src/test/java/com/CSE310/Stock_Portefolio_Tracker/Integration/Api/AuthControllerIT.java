package com.CSE310.Stock_Portefolio_Tracker.Integration.Api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.CSE310.Stock_Portefolio_Tracker.TestSecurityConfig;
import com.CSE310.Stock_Portefolio_Tracker.Dto.LoginRequestDto;
import com.CSE310.Stock_Portefolio_Tracker.Dto.SignupResponseDto;
import com.CSE310.Stock_Portefolio_Tracker.ExternalApi.StockApiClient;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;





@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
public class AuthControllerIT {
    @LocalServerPort
    int port;
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api/stockportefoliotracker/v1"; // ✅ IMPORTANT
    }

   
    @MockBean
    private StockApiClient stockApiClient;



    @Test
    void register_user_successfully() {
        
            
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                "username":"acho",
                "email":"acho@gmail.com",
                "password":"acho"
                }
            """)
        .when()
            .post("/register")
        .then()
            .statusCode(201)
            .body("statusMsg", equalTo("USER CREATED SUCCESSFULLY"));
}

    

@Test
void login_successfully() {

    LoginRequestDto request = new LoginRequestDto();
    request.setUsername("acho@gmail.com");
    request.setPassword("acho");

    given()
        .contentType(ContentType.JSON)
        .body(request)
    .when()
        .post("/login")
    .then()
        .statusCode(200)
        .body("token", notNullValue())
        .body("refresh", notNullValue());
}

    @Test
        void should_fail_login_with_invalid_credentials() {

            LoginRequestDto request = new LoginRequestDto();
            request.setUsername("yattex@gmail.com"); // utilisateur qui n'existe pas
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
        



       @Test
        void should_login_and_refresh_token_successfully() {
            // 1️⃣ Login pour obtenir token et refreshToken
            LoginRequestDto loginRequest = new LoginRequestDto();
            loginRequest.setUsername("acho@gmail.com");
            loginRequest.setPassword("acho");

            SignupResponseDto loginTokens = given()
                    .contentType(ContentType.JSON)
                    .body(loginRequest)
                    .log().all()
                .when()
                    .post("/login")
                .then()
                    .log().all()
                    .extract()
                    .as(SignupResponseDto.class);
                   
                    // Vérification des tokens reçus
                    assertNotNull(loginTokens.getToken(), "Access token should not be null");
                    assertNotNull(loginTokens.getRefresh(), "Refresh token should not be null");
                    
                

           
        }


    }
