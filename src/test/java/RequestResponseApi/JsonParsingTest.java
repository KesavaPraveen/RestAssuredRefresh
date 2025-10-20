package RequestResponseApi;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import java.util.List;

public class JsonParsingTest {

    /*

        🧩 Let’s Begin with Task 1

        Task 1 — JSONPath Extraction Practice

        🔗 Endpoint: https://reqres.in/api/users?page=2

        Goals:
            •	Extract first name of first user
            •	Extract all emails as a list
            •	Count total users in data array
            •	Validate support.text is not null
     */

    @Test
    public void jsonExtractionJsonPath()
    {
        Response response=RestAssured.given().baseUri("https://reqres.in")
                .header("x-api-key", "reqres-free-v1")
                .queryParam("page",2)
                .when().get("/api/users")
                .then()
                .body("data[0].first_name",equalTo("Michael"))
                .body("data.size()",equalTo(6))
                .body("support.text",notNullValue()).extract().response();

        String firstNameUser1=response.jsonPath().get("data[0].first_name");
        System.out.println(firstNameUser1);
        List<String> emailList=response.jsonPath().getList("data.email");
        System.out.println(emailList);
        int usersCount=response.jsonPath().getList("data.id").size();
        System.out.println(usersCount);
        int users=response.jsonPath().getInt("data.size()");
        System.out.println("Size: " +users);
        String support=response.jsonPath().get("support.text");
        Assert.assertTrue(!support.isEmpty());
    }

    /*
        🧩 Task 2 — Complex JSON Extraction (Groovy GPath)

    Endpoint:
    https://reqres.in/api/users?page=2

    🎯 Objectives:
    1️⃣ Extract all user first_name where id > 9
    2️⃣ Extract the email of the user whose first_name = “Rachel”
    3️⃣ Validate total_pages = 2 using Hamcrest
    4️⃣ Print both results for confirmation
     */
    @Test
    public void complexJsonExtractionGpathTest()
    {
        Response response=RestAssured.given().baseUri("https://reqres.in")
                .header("x-api-key", "reqres-free-v1")
                .queryParam("page",2)
                .when().get("/api/users")
                .then()
                .body("total_pages",equalTo(2)).extract().response();

        List<String> usersList=response.jsonPath().getList("data.findAll { it.id > 9 }.first_name");
        System.out.println("Users First Name where Id > 9: " +usersList);

        String rachelEmail= response.jsonPath().get("data.find {it.first_name == 'Rachel' }.email");
        System.out.println("Rachels Email: " +rachelEmail);
        Assert.assertTrue(rachelEmail.endsWith("@reqres.in"), "Email domain mismatch");
    }
}
