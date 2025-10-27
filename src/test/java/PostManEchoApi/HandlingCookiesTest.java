package PostManEchoApi;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;
import static org.hamcrest.Matchers.*;
public class HandlingCookiesTest {

    /*
    🔹 Step 2: What to do
	1.	Send a GET request to the above endpoint.
	2.	Extract all cookies from the response.
	3.	Print all cookies in the console.
	4.	Validate the following using TestNG assertions:
	•	Cookie names include session_id and user.
	•	The value of user is "Kesava".
	•	Cookie count > 0.
	Use: https://postman-echo.com/cookies/set?session_id=12345&user=Kesava
     */

    @Test
    public void cookieHandleTest()
    {
        Response response=RestAssured.given().baseUri("https://postman-echo.com")
                .queryParam("session_id","1234567")
                .queryParam("user","KesavaPraveen")
                .when().get("/cookies/set")
                .then()
                .log().body()
                .log().cookies()
                .extract().response();

        Map<String,String> cookies=response.getCookies();
        System.out.println(cookies);
        Assert.assertTrue(cookies.containsKey("__cf_bm"));
    }

    /*
    🧱 Step 2 — Steps to Implement
	1.	Send a GET request to /cookies
    – Add two cookies manually:
	•	session_id = 12345
	•	user = KesavaPraveen
	2.	Extract the response body as a Map.
	3.	Print the cookies returned.
	4.	Assert:
	•	JSON body has key "cookies".
	•	session_id = "12345".
	•	user = "KesavaPraveen".
	•	Cookie count ≥ 2.
     */

    @Test
    public void cookiesAssertionTest()
    {
        Response response=RestAssured.given().baseUri("https://postman-echo.com")
                .cookie("session_id","12345678")
                .cookie("user","KesavaPraveen")
                .when().get("/cookies")
                .then().extract().response();

        Map<String,Object> res=response.jsonPath().getMap("$");
        System.out.println(res);
        Map<String,Object> cookie=response.jsonPath().getMap("cookies");
        System.out.println(cookie);
        Assert.assertTrue(res.containsKey("cookies"));
        Assert.assertEquals(cookie.get("session_id"),"12345678");
        Assert.assertEquals(cookie.get("user"),"KesavaPraveen");
        int cookieCount=cookie.size();
        Assert.assertTrue(cookieCount>=2);
    }
}
