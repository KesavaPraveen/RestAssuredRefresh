package JsonPlaceHolderApi;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class nestedJsonValidationWithHashMap {

    /*
            🧩 Task — Nested JSON Validation with HashMap

        Endpoint:
        🔗 https://jsonplaceholder.typicode.com/users/1

        Steps:
        1️⃣ Send a GET request to the endpoint.
        2️⃣ Convert the JSON response into a Map<String, Object>.
        3️⃣ Extract the nested address object → it’s another map.
        4️⃣ Inside address, extract the nested geo object.
        5️⃣ Assert:
            •	"address.city" = "Gwenborough"
            •	"geo.lat" = "-37.3159"
            •	"geo.lng" = "81.1496"
     */

    @Test
    public void nestedJsonTest()
    {
        Response response=RestAssured.given().baseUri("https://jsonplaceholder.typicode.com")
                .when().get("/users/1")
                .then()
                .log().body()
                .body("address.city", equalTo("Gwenborough"))
                .body("address.geo.lat", equalTo("-37.3159"))
                .body("address.geo.lng", equalTo("81.1496"))
                .extract().response();

        Map<String,Object> resMap=response.jsonPath().getMap("$");
        Map<String,Object> addressMap=response.jsonPath().getMap("address");
        Map<String,Object> geoMap=response.jsonPath().getMap("address.geo");

        System.out.println(resMap);
        System.out.println(addressMap);
        System.out.println(geoMap);

        Assert.assertEquals(addressMap.get("city"),"Gwenborough");
        Assert.assertEquals(geoMap.get("lat"),"-37.3159");
        Assert.assertEquals(geoMap.get("lng"),"81.1496");

    }
}
