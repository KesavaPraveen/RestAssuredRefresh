import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import jdk.jfr.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
public class PostManEchoApiTest {
    @Test
    public void getRequestHeaderValidation() {
        RestAssured.baseURI = "https://postman-echo.com";

        RestAssured.given()
                .log().all()
                .when()
                .get("/get?foo=bar")
                .then()
                .log().all()
                .header("Content-Type", "application/json; charset=utf-8")
                .body("args.foo", equalTo("bar"));
    }

    /*
        2️⃣ Headers & Cookies Extraction

    🔗 https://postman-echo.com/get?foo=bar

    Practice Goals:
        •	Print all headers
        •	Print cookies
        •	Assert Content-Type
        •	Check a specific header value
     */

    @Test
    public void extractHeaderAndCookie()
    {
        Response response=RestAssured.given()
                .log().all()
                .when()
                .get("https://postman-echo.com/get?foo=bar")
                .then()
                .log().headers()
                .log().cookies()
                .header("Content-Encoding",equalTo("gzip"))
                .extract().response();

        Headers headers=response.getHeaders();
        System.out.println("Headers: " +headers);
        System.out.println("-----------------");
        Map<String,String> cookies=response.getCookies();
        System.out.println("Cookies: " +cookies);
        String expectedHeaderContentType="application/json; charset=utf-8";
        String actualHeaderContentType=response.getHeader("Content-Type");
        Assert.assertEquals(actualHeaderContentType
                ,expectedHeaderContentType,"Mismatch in the Content Type ");

    }


}
