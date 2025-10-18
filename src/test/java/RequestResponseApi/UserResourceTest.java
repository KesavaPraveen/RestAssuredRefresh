package RequestResponseApi;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.Matchers.*;

public class UserResourceTest extends BaseApiTest {

    /*
        🧩 Task 1 — POST Request (Create Resource)
        Endpoint: https://reqres.in/api/users
        Steps:
        1️⃣ Send JSON body → { "name": "Kesava", "job": "QA Engineer" }
        2️⃣ Set header Content-Type: application/json
        3️⃣ Validate:
        • status = 201
        • id present
        • createdAt present
     */
    @Test
    public void createUserTest() {
        String user = "{ \"name\": \"Kesava\", \"job\": \"QA Engineer\" }";
        Response response = RestAssured.given().baseUri("https://reqres.in")
                //.log().all()
                .header("x-api-key", "reqres-free-v1")
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/api/users")
                .then().log().all()
                .assertThat().statusCode(201)
                .body("id", notNullValue())
                .body("createdAt", notNullValue())
                .extract().response();

        String id = response.jsonPath().get("id");
        System.out.println("ID: " + id);
    }

    /*
            🧩 Task 2 — PUT Request (Update Entire Resource)
        Endpoint: https://reqres.in/api/users/2
        Steps:
        1️⃣ Body → { "name": "Kesava", "job": "Senior QA Engineer" }
        2️⃣ Send with header Content-Type: application/json
        3️⃣ Validate:
        • status = 200
        • job = Senior QA Engineer
        • updatedAt present
        Expected Output:
        ✔ Update succeeds with 200
        ✔ Response includes new job and timestamp
     */

    @Test
    public void updateUserTest() {
        String userUpdate = "{ \"name\": \"Kesava\", \"job\": \"Senior QA Engineer III\" }";
        Response response = RestAssured.given().baseUri("https://reqres.in")
                //.log().all()
                .header("x-api-key", "reqres-free-v1")
                .contentType(ContentType.JSON)
                .body(userUpdate)
                .when().put("/api/users/2")
                .then()
                //.log().all()
                .assertThat().statusCode(200)
                .body("job", equalToIgnoringCase("Senior QA Engineer III"))
                .body("updatedAt", notNullValue())
                .extract().response();

        String updatedTime = response.jsonPath().getString("updatedAt");
        System.out.println("Updated the RequestResponseApi.User record At: " + updatedTime);
    }

    /*
            🧩 Task 3 — PATCH Request (Partial Update)
        Endpoint: https://reqres.in/api/users/2
        Steps:
        1️⃣ Body → { "job": "API Specialist" }
        2️⃣ Validate:
        • status = 200
        • job = API Specialist
        • updatedAt exists

        Expected Output:
        ✔ Partial update successful
        ✔ Only the job field changes
     */
    @Test
    public void partialUserUpdateTest() {
        String partialUpdate = "{ \"job\": \"API Specialist\" }";
        Response response = RestAssured.given().baseUri("https://reqres.in")
                //.log().all()
                .header("x-api-key", "reqres-free-v1")
                .contentType(ContentType.JSON)
                .body(partialUpdate)
                .when().patch("/api/users/2")
                .then()
                //.log().all()
                .assertThat().statusCode(200)
                .body("job", equalToIgnoringCase("API Specialist"))
                .body("updatedAt", notNullValue())
                .extract().response();

        String updatedJob = response.jsonPath().getString("job");
        System.out.println("Udpated Job: " + updatedJob);
        String updatedTime = response.jsonPath().getString("updatedAt");
        System.out.println("Updated the RequestResponseApi.User record At: " + updatedTime);
    }

    /*
    🧩 Task 4 — DELETE Request (Remove Resource)
        Endpoint: https://reqres.in/api/users/2
        Steps:
        1️⃣ No body needed
        2️⃣ Send DELETE
        3️⃣ Validate status = 204 and empty body
     */
    @Test
    public void deleteUserTest() {
        RestAssured.given()
                //.log().all()
                .header("x-api-key", "reqres-free-v1")
                .when().delete("https://reqres.in/api/users/2")
                .then()
                //.log().all()
                .assertThat().statusCode(204);
    }

    /*
    🧩 Task 5 — Query Parameter (Filter Data)

    Endpoint: https://reqres.in/api/users?page=2

    Steps:
    1️⃣ Add .queryParam("page", 2)
    2️⃣ Validate: page = 2, data not empty

    Expected Output:
    ✔ URI shows ?page=2
    ✔ RequestResponseApi.User list for page 2 displayed
     */

    @Test
    public void getDetailsWithQueryParamTest() {
        Response response = RestAssured.given().baseUri("https://reqres.in")
                .queryParam("page", "2")
                //.log().all()
                .header("x-api-key", "reqres-free-v1")
                .when().get("/api/users")
                .then()
                //.log().all()
                .assertThat().statusCode(200)
                .body("page", equalTo(2))
                .extract().response();
        Map<String, String> body = response.jsonPath().getMap("");
        System.out.println(body);

        /*String header=response.getHeader("Request URI");
        System.out.println(header);
        Assert.assertTrue(header.contains("?page=2"));
        header("Request URI",containsString("?page=2"))
         */
    }

    /*
        🧩 Task 6 — Path Parameter (Dynamic Resource)

        Endpoint Template: https://reqres.in/api/users/{userId}

        Steps:
        1️⃣ Set .pathParam("userId", 2)
        2️⃣ Validate: data.id = 2 and email contains @reqres.in

        Expected Output:
        ✔ URI logs with ID 2
        ✔ Response shows correct user
     */
    @Test
    public void getDetailWithPathParamTest()
    {
        Response response=RestAssured.given().baseUri("https://reqres.in")
                .pathParam("userId",2)
                //.log().all()
                .header("x-api-key", "reqres-free-v1")
                .when().get("/api/users/{userId}")
                .then()
                .assertThat().statusCode(200)
                //.log().all()
                .body("data.id",equalTo(2))
                .extract().response();

    }

    /*
    🧩 Flow Summary
        1️⃣ POST → Create user
        ✅ Validate 201
        ✅ Extract the "id"

        2️⃣ PUT or PATCH → Update same user
        ✅ Use the extracted "id" dynamically in your endpoint
        ✅ Validate 200 and "updatedAt"

        3️⃣ DELETE → Remove the same user
        ✅ Use same "id"
        ✅ Validate 204

        4️⃣ (Optional) GET → Verify deletion
        ✅ Expect status 404 or empty data
     */

    @Test
    public void crudUserTest()
    {

        /*
        Creating a new RequestResponseApi.User
         */
        String user = "{ \"name\": \"Praveen\", \"job\": \"Senior Automation Engineer\" }";

        Response postResponse = RestAssured.given()
                .baseUri("https://reqres.in")
                .header("x-api-key", "reqres-free-v1")
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/api/users")
                .then()
                //.log().body()
                .assertThat().statusCode(201)
                .extract().response();

        String id=postResponse.jsonPath().getString("id");
        System.out.println("Created New RequestResponseApi.User ID: " +id);

        /*
        Updating the RequestResponseApi.User Job Detail
         */
        System.out.println("Updating the name of the user " +id);
        String userUpdate = "{ \"name\": \"Kesava\", \"job\": \"Senior API Automation Engineer\" }";
        RestAssured.given().baseUri("https://reqres.in")
                .pathParam("userId",id)
                .header("x-api-key", "reqres-free-v1")
                .contentType(ContentType.JSON)
                .body(userUpdate)
                .when().put("/api/users/{userId}")
                .then()
                //.log().body()
                .assertThat().statusCode(200)
                .body("name",equalTo("Kesava"))
                .body("updatedAt", notNullValue());
        System.out.println("Updated the name of the user " +id);

        /*
        Deleting the RequestResponseApi.User
         */
        System.out.println("Deleting the user " +id);
        RestAssured.given()
                .pathParam("userId",id)
                .header("x-api-key", "reqres-free-v1")
                .when().delete("https://reqres.in/api/users/{userId}")
                .then()
                .assertThat().statusCode(204);
        System.out.println("Deleted the user");
        /*
        Validating the user is present after deletion and getting 404 status
         */
        System.out.println("Checking the deleted user is present");
        RestAssured.given()
               // .log().all()
                .pathParam("userId",id)
                .header("x-api-key", "reqres-free-v1")
                .when().get("https://reqres.in/api/users/{userId}")
                .then()
               // .log().all()
                .assertThat().statusCode(404);
    }

    @Test
    public void specBuilderCrudTest()
    {
        String user = "{ \"name\": \"Praveen\", \"job\": \"Senior Automation Engineer\" }";

        Response response=RestAssured.given().spec(SpecBuilderUtil.getRequestSpecBuilder())
                .body(user)
                .when().post("/api/users")
                .then().spec(SpecBuilderUtil.getResponseSpecBuilder(201))
                .extract().response();

        String id=response.jsonPath().getString("id");

        String userUpdate = "{ \"name\": \"Kesava\", \"job\": \"Senior API Automation Engineer\" }";

        RestAssured.given().spec(SpecBuilderUtil.getRequestSpecBuilder())
                .pathParam("userId",id)
                .body(userUpdate)
                .when().put("/api/users/{userId}")
                .then().spec(SpecBuilderUtil.getResponseSpecBuilder(200));

        RestAssured.given().spec(SpecBuilderUtil.getRequestSpecBuilder())
                .pathParam("userId",id)
                .when().delete("/api/users/{userId}")
                .then().spec(SpecBuilderUtil.getResponseSpecBuilder(204));

        RestAssured.given().spec(SpecBuilderUtil.getRequestSpecBuilder())
                .pathParam("userId",id)
                .when().get("/api/users/{userId}")
                .then().spec(SpecBuilderUtil.getResponseSpecBuilder(404));
    }

}

