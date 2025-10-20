package RequestResponseApi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class NegativeTest {

    @Test
    public void negativeInvalidPayloadTest() {

        // ❌ malformed JSON body
        String invalidPayload = "{ \"name\": , \"job\": }";

        RestAssured.given()
                .baseUri("https://reqres.in")
                .header("x-api-key", "reqres-free-v1")
                .contentType(ContentType.JSON)
                .body(invalidPayload)
                .when()
                .post("/api/users")
                .then()
                .log().body()
                .assertThat()
                .statusCode(anyOf(is(400), is(415))); // some APIs use 415 for malformed body
    }

    @Test
    public void negativeWrongMethodTest() {

        // ❌ calling PUT on a GET-only endpoint
        RestAssured.given()
                .baseUri("https://reqres.in")
                .header("x-api-key", "reqres-free-v1")
                .when()
                .put("/api/unknown")
                .then()
                .log().body()
                .assertThat()
                .statusCode(anyOf(is(404), is(405)));
    }

    @Test
    public void negativeResourceNotFoundTest() {

        // ❌ invalid user ID
        RestAssured.given()
                .baseUri("https://reqres.in")
                .header("x-api-key", "reqres-free-v1")
                .when()
                .get("/api/users/9999")
                .then()
                .log().body()
                .assertThat()
                .statusCode(404);
    }
}
