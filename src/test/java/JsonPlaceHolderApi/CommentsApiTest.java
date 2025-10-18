package JsonPlaceHolderApi;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class CommentsApiTest {
    /*
    🧩 Challenge:
	•	Use https://jsonplaceholder.typicode.com/comments?postId=1
	•	Validate:
	•	Status code = 200
	•	Total records = 5
	•	First comment’s email contains “@”
	•	Extract all name fields into a List and print
     */

    @Test
    public void getRequestValidationTest()
    {
        Response response=RestAssured.given().baseUri("https://jsonplaceholder.typicode.com")
                .log().all()
                .when()
                .get("/comments?postId=1")
                .then()
                .log().all()
                .statusCode(200)
                // Validating the email key count in the response body
                .body("email.size()",equalTo(5))
                // Validating the first comment of email contains the special char "@"
                .body("email[0]",containsString("@"))
                .extract()
                .response();

        /*
        Extracting the ID and its size and asserting to check the total record count
         */
        List<Integer> totalRecords=response.jsonPath().getList("id");
        int count=totalRecords.size();
        System.out.println("Total Records Count: " +count);
        int expectedCount=5;
        Assert.assertEquals(count,expectedCount,"Record count mismatch!");

        /*
        Extracting all the names and printing in the console
         */
        List<String> namesList=response.jsonPath().getList("name");
        System.out.println("Names List: " +namesList);
    }


    /*
        1️⃣ Negative Query Parameter Case
        🔗 https://jsonplaceholder.typicode.com/comments?postId=abc
    Practice Goals:
        •	Observe and log the actual response
        •	Validate:
        •	Status code (expect 200, but empty array)
        •	Body list size = 0
        •	Time under 2000 ms
     */
    @Test
    public void negativeQueryParamTest()
    {
       Response response= RestAssured.given().baseUri("https://jsonplaceholder.typicode.com")
               .log().all()
                .when()
                .get("/comments?postId=abc")
                .then()
               .log().all()
                .assertThat()
                .statusCode(200)
                .extract().response();

       int size=response.jsonPath().getList("").size();
       Assert.assertEquals(size,0,"Expected empty response");
       long responseTime=response.time();
       Assert.assertTrue(responseTime<2000,"Response time is more than 2000 ms!");

    }

}
