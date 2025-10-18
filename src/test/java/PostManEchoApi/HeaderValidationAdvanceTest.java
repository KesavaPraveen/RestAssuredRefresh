package PostManEchoApi;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HeaderValidationAdvanceTest {
    /*
    🧩 Header Validation (Multi-Header + Hamcrest Style)
     */

    @Test
    public void headerValidationTest()
    {
        Response response=RestAssured.given().baseUri("https://postman-echo.com")
                .when().get("/get?foo=bar")
                .then()
                .extract().response();
        /*
        Extracting and Printing all the headers
         */
        List<Header> headers=response.getHeaders().asList();
        Map<String,String> headerMap=new LinkedHashMap<>();
        for(Header h: headers)
        {
            headerMap.put(h.getName(),h.getValue());
            System.out.println(h.getName()+":" +h.getValue());
        }

        /*
        Validating the headers using TestNG Assertion
         */
        Assert.assertTrue(headerMap.containsKey("Content-Type"));
        Assert.assertTrue(headerMap.containsKey("Connection"));

        String expectedContentType="application/json; charset=utf-8";
        String actualContentType=headerMap.get("Content-Type");
        Assert.assertEquals(actualContentType,expectedContentType);

        String expectedConnection="keep-alive";
        String actualConnection=headerMap.get("Connection");
        Assert.assertEquals(actualConnection,expectedConnection);

        /*
        Validating using hamcrest assertion
         */
        assertThat(headerMap,hasKey("Server"));
        assertThat(headerMap,hasKey("vary"));
        assertThat(headerMap.get("Server"),containsString("cloudflare"));

        //System.out.println(headers);
    }
}
