package RequestResponseApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import java.util.HashMap;
import java.util.Map;

public class PayloadBuildTest {

    /*
    🧩 1️⃣ HashMap Payload Practice (Dynamic JSON Building)
    💻 Task:
    1️⃣ Create a HashMap<String, Object>
    2️⃣ Add keys "name" and "job" with your values.
    3️⃣ Send a POST request to https://reqres.in/api/users.
    4️⃣ Validate:
    ✅ Status = 201
    ✅ Response contains "id" and "createdAt"
     */
    @Test
    public void hashMapPayloadPractice()
    {
        Map<String, Object> payload=new HashMap<>();
        payload.put("name","Ramu");
        payload.put("job","Senior API Tester II");
        Response response=RestAssured.given()
                .contentType(ContentType.JSON)
                .header("x-api-key", "reqres-free-v1")
                .body(payload)
                .when().post("https://reqres.in/api/users")
                .then()
                .log().body()
                .body("",hasKey("job"))
                .assertThat().statusCode(201)
                .extract().response();
        Map<String,Object> responsePayLoad=response.jsonPath().getMap("$");
        Assert.assertTrue(responsePayLoad.containsKey("id"));
        Assert.assertTrue(responsePayLoad.containsKey("createdAt"));
    }

    /*
        🧩 3️⃣ RequestSpecification Mini Practice
        🎯 Goal:
        Avoid repeating base URI, headers, and content type in each request
        💻 Task:
        1️⃣ Create a RequestSpecification:
        2️⃣ Use it in a simple GET or POST request:
     */
    @Test
    public void practiceRequestSpecification()
    {
        /*
        Creating Request Spec Builder
         */
        RequestSpecification requestSpecification=new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .addHeader("x-api-key", "reqres-free-v1")
                .setContentType(ContentType.JSON)
                .build();
        /*
        Creating a new RequestResponseApi.User
         */
        String user = "{ \"name\": \"Praveen\", \"job\": \"Senior Automation Engineer\" }";

        Response postResponse = RestAssured.given().spec(requestSpecification)
                .body(user)
                .when().post("/api/users")
                .then()
                .assertThat().statusCode(201)
                .extract().response();

        String id=postResponse.jsonPath().getString("id");
        System.out.println("Created New RequestResponseApi.User ID: " +id);

        /*
        Updating the RequestResponseApi.User Job Detail
         */
        System.out.println("Updating the name of the user " +id);

        String userUpdate = "{ \"name\": \"Kesava\", \"job\": \"Senior API Automation Engineer\" }";
        RestAssured.given().spec(requestSpecification)
                .pathParam("userId",id)
                .body(userUpdate)
                .when().put("/api/users/{userId}")
                .then()
                .assertThat().statusCode(200);

        /*
        Deleting the RequestResponseApi.User
         */
        System.out.println("Deleting the user " +id);
        RestAssured.given().spec(requestSpecification)
                .pathParam("userId",id)
                .when().delete("https://reqres.in/api/users/{userId}")
                .then()
                .assertThat().statusCode(204);
        System.out.println("Deleted the user");

        /*
        Validating the user is present after deletion and getting 404 status
         */
        System.out.println("Checking the deleted user is present");
        RestAssured.given()
                .spec(requestSpecification)
                .pathParam("userId",id)
                .when().get("https://reqres.in/api/users/{userId}")
                .then()
                .assertThat().statusCode(404);
    }

    @Test
    public void objectMapperSerialization() throws JsonProcessingException {
        /*
        Serialization
         */
        ObjectMapper objectMapper=new ObjectMapper();
        User user=new User("Saro","Test Lead");

        String requestPayload=objectMapper.writeValueAsString(user);
        System.out.println("Request Payload with Object Mapper Serialization: " +requestPayload);

        /*
        Deserialization
         */
        String responsePayload="{ \"name\": \"Praveen\", \"job\": \"Senior Automation Engineer\" }";
        User deserializeUser=objectMapper.readValue(responsePayload, User.class);
        System.out.println("Name: " +deserializeUser.getName());
        System.out.println("Job: " +deserializeUser.getJob());
    }
}
