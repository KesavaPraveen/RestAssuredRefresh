package PostManEchoApi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
public class AuthenticationTest {

    @Test
    public void basicAuthTestSuccess()
    {
        Response response=RestAssured.given()
                .auth().basic("postman","password")
                .when().get("https://postman-echo.com/basic-auth")
                .then()
                .log().body()
                .assertThat().statusCode(200)
                .extract().response();

        Assert.assertEquals(response.jsonPath().getString("authenticated"),"true");
    }

    @Test
    public void basicAuthTestFailure()
    {
        RestAssured.given()
                .auth().basic("postman","passwrd")
                .when().get("https://postman-echo.com/basic-auth")
                .then()
                .log().body()
                .assertThat().statusCode(401);
    }

    @Test
    public void preemptiveAuthTestSuccess()
    {
        Response response=RestAssured.given()
                .auth().preemptive().basic("postman","password")
                .when().get("https://postman-echo.com/basic-auth")
                .then()
                .log().body()
                .assertThat().statusCode(200)
                .extract().response();

        Assert.assertEquals(response.jsonPath().getString("authenticated"),"true");
    }

    @Test
    public void preemptiveAuthTestFailure()
    {
        Response response=RestAssured.given()
                .auth().preemptive().basic("postman","wrongpass")
                .when().get("https://postman-echo.com/basic-auth")
                .then()
                .log().headers()
                .log().body()
                .body(equalTo("Unauthorized"))
                .assertThat().statusCode(401).extract().response();

        String header=response.getHeaders().get("www-authenticate").toString();
        System.out.println(header);
        Assert.assertTrue(header.contains("Basic realm"));
    }

    /*
            🧩 1️⃣ bearerTokenValidTest() — Success Flow
        🧱 Test Goal
        Validate API access using a valid Bearer token.
        🧭 Steps
            1.	Set Base URI → https://gorest.co.in/public/v2
            2.	Attach Token → .auth().oauth2("<your_valid_token_here>")
            3.	GET Request → /users
            4.	Assertions
            •	✅ Status code = 200
            •	✅ Content-Type = application/json
            •	✅ Response is an array (size > 0)
            •	✅ Optional: Print the first user’s name or email for verification.
     */

    @Test
    public void bearerTokenValidTest()
    {
        Response response=RestAssured.given().baseUri("https://gorest.co.in/public/v2")
                .auth().oauth2("e0b2a17712f0dc85a4a6601c607268fb9804cd794e54106b33499eda2681dcc2")
                .when().get("/users")
                .then()
                .log().body()
                .log().headers()
                .assertThat().statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();

        int size=response.jsonPath().getList("$").size();

        String name=response.jsonPath().get("[0].name");
        System.out.println(name);

        Assert.assertTrue(size > 0);
    }

    /*
        🧩 2️⃣ bearerTokenInvalidTest() — Failure Flow
    🧱 Test Goal
    Verify that the API rejects an invalid token properly.
    🧭 Steps
        1.	Use .auth().oauth2("invalid_token")
        2.	Send the same GET request → /users
        3.	Assertions
        •	❌ Status code = 401
        •	❌ Body contains "message": "Invalid token" (or similar)
        •	✅ Log headers/body for debugging
     */

    @Test
    public void bearerTokenInvalidTest()
    {
       RestAssured.given().baseUri("https://gorest.co.in/public/v2")
                .auth().oauth2("invalid_token")
                .when().get("/users")
                .then()
                .log().body()
                .log().headers()
                .log().body()
                .assertThat().statusCode(401)
                .body("message",equalTo("Invalid token"));

    }

    /*
    🧱 Test 1 — digestAuthTestSuccess()
🧭 Steps:
	1.	Base URI: https://postman-echo.com
	2.	Endpoint: /digest-auth
	3.	Auth: .auth().digest("postman", "password")
	4.	Assertions:
	•	✅ Status code = 200
	•	✅ "authenticated" = true
	•	✅ Log the headers/body for understanding
     */

    @Test
    public void digestAuthTestSuccess()
    {
        RestAssured.given().baseUri("https://postman-echo.com")
                .auth().digest("postman", "password")
                .when().get("/digest-auth")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .body("authenticated",equalTo(true));
    }

    /*
    🧱 Test 2 — digestAuthTestFailure()
    🧭 Steps:
        1.	Use wrong password → "postman", "wrongpass"
        2.	Expectations:
        •	❌ Status code = 401
        •	❌ "authenticated" = false or "Unauthorized" message
     */

    @Test
    public void digestAuthTestFailure()
    {
        RestAssured.given().baseUri("https://postman-echo.com")
                .auth().digest("postman", "wrongpass")
                .when().get("/digest-auth")
                .then()
                .log().all()
                .assertThat().statusCode(401)
                .body(equalTo("Unauthorized"));
    }

    @Test
    public void simulatingOAuth2Test()
    {
        String req="{\n" +
                "  \"access_token\": \"mock_token_123\",\n" +
                "  \"token_type\": \"Bearer\"\n" +
                "}" ;
        Response response=RestAssured.given().baseUri("https://postman-echo.com")
                .contentType(ContentType.JSON)
                .body(req)
                .when().post("/post")
                .then()
                .log().body()
                .assertThat().statusCode(200)
                .extract().response();

        String token=response.jsonPath().getString("data.access_token");
        System.out.println(token);

        RestAssured.given().baseUri("https://postman-echo.com")
                .header("Authorization","Bearer " + token)
                .when().get("/get")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .contentType(ContentType.JSON);
    }
}
