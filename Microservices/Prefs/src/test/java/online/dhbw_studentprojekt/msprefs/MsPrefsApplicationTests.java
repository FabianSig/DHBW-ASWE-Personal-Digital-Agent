package online.dhbw_studentprojekt.msprefs;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MsPrefsApplicationTests {

    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

    static {
        mongoDBContainer.start();
    }

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void contextLoads() {

    }

//    @Test
//    void shouldGetPref() {
//        RestAssured.given()
//                .get("/api/prefs")
//    }

    @Test
    void shouldCreatePref() {

        String requestBody = """
                {
                    "id": "testpref",
                    "value": [
                        "value1",
                        "value2"
                    ]
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .header("Authorization", System.getenv("OUR_API_KEY"))
                .body(requestBody)
                .when()
                .post("/api/prefs")
                .then()
                .statusCode(201);
    }

}
