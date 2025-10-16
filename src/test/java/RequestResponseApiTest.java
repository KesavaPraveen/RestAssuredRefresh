import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
public class RequestResponseApiTest {
    @Test
    public void getRequestStatusCodeValidation()
    {


        RestAssured.baseURI="https://reqres.in";
                RestAssured.given()
                        .log().all()
                        .when().get("/api/users?page=2")
                        .then()
                        .log().all()
                        .statusCode(200);

                /*
                Create a reusable get method
                Response response=getRequest("https://reqres.in/api/users?page=2",200);
                 */
    }
    public Response getRequest(String uri,int expStatusCode)
    {
        return RestAssured.given().log().all().when().get(uri)
                .then().log().all().statusCode(expStatusCode).extract().response();
    }
    @Test
    public void getRequestBodyValidation()
    {
        RestAssured.baseURI="https://reqres.in";
        RestAssured.given()
                .log().all()
                .when().get("/api/users?page=2")
                .then()
                .log().all()
                .body("data[0].id",equalTo(7))
                .body("data[0].email",containsString("@reqres.in"));
    }

    @Test
    public void getRequestDataExtract()
    {
        RestAssured.baseURI="https://reqres.in";
        Response response=RestAssured.given()
                .log().all()
                .when().get("/api/users?page=2")
                .then()
                .log().all()
                .extract()
                .response();

        String firstName=response.jsonPath().getString("data[0].first_name");
        System.out.println("First Name: " +firstName);

        String lastName=response.jsonPath().getString("data[0].last_name");
        System.out.println("Last Name: " +lastName);

        String email=response.jsonPath().getString("data[0].email");
        System.out.println("Email: " +email);

        List<String> firstNames=response.jsonPath().getList("data.first_name");
        System.out.println("First Names List: " +firstNames);

        List<String> lastNames=response.jsonPath().getList("data.last_name");
        System.out.println("Last Names List: " +lastNames);

        List<String> emailIds=response.jsonPath().getList("data.email");
        System.out.println("Email Ids List: " +emailIds);

    }

    @Test
    public void invalidEndPointValidation()
    {
        RestAssured.baseURI="https://reqres.in";
        RestAssured.given()
                .log().uri()
                .when()
                .get("/api/users/23/invalid")
                .then()
                .log().body()
                //.log().ifValidationFails()
                .statusCode(401)
                .body("error",equalTo("Missing API key"));
    }


}
