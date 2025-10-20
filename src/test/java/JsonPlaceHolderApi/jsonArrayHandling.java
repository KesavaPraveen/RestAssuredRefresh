package JsonPlaceHolderApi;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class jsonArrayHandling {
    /*
            1️⃣ Send GET request to the endpoint above.
            2️⃣ Validate:
                •	Status code = 200
                •	Total records = 5
                •	Every email in response contains "@"
            3️⃣ Extract all name fields and print them.
            4️⃣ Assert that every id > 0
     */

    @Test
    public void jsonArrayTest()
    {
        Response response=RestAssured.given().baseUri("https://jsonplaceholder.typicode.com")
                .queryParam("postId",1)
                .when().get("/comments")
                .then()
                .log().body()
                .assertThat().statusCode(200)
                .extract().response();

        List<Object> res=response.jsonPath().getList("$");
        List<String> emailList=response.jsonPath().getList("email");
        for(String s: emailList)
        {
           Assert.assertTrue(s.contains("@"));
        }
        Assert.assertEquals(res.size(),5);
        System.out.println(res);
        System.out.println(emailList);
        List<String> names=response.jsonPath().getList("name");
        System.out.println(names);

        List<Integer> ids=response.jsonPath().getList("id");
        for(int num:ids)
        {
            Assert.assertTrue(num>0);
        }

        assertThat(emailList, everyItem(containsString("@")));
        assertThat(ids, everyItem(greaterThan(0)));
        assertThat(names.size(), equalTo(5));
    }

    /*
        1️⃣ Extract full JSON as a Map<String, Object>
        2️⃣ Extract nested sections (like address, company) as separate Maps
        3️⃣ Extract list or nested objects dynamically
        4️⃣ Assert key–value pairs from those maps
        ✅ Validations:
        •	address.city == "Gwenborough"
        •	geo.lat == "-37.3159"
        •	geo.lng == "81.1496"
     */

    @Test
    public void extractFullJsonTest()
    {
        Response response=RestAssured.given().baseUri("https://jsonplaceholder.typicode.com")
                .when().get("/users/1")
                .then()
                .log().body()
                .extract().response();
        Map<String,Object> resMap=response.jsonPath().getMap("$");
        System.out.println(resMap);
        Map<String,Object> addMap=response.jsonPath().getMap("address");
        System.out.println(addMap);
        Map<String,Object> geoMap=response.jsonPath().getMap("address.geo");
        System.out.println(geoMap);
        Map<String,Object> compMap=response.jsonPath().getMap("company");
        System.out.println(compMap);

        Assert.assertTrue(addMap.containsKey("city"));
        Assert.assertTrue(addMap.containsValue("Gwenborough"));

        assertThat(resMap,hasKey("website"));
        Assert.assertTrue(geoMap.containsValue("-37.3159"));
        Assert.assertTrue(geoMap.containsValue("81.1496"));
        for(Map.Entry<String,Object> entry:geoMap.entrySet())
        {
            if(entry.getKey().equals("lat")) {
                Assert.assertTrue(entry.getValue().equals("-37.3159"));
            }
            if(entry.getKey().equals("lng")) {
                Assert.assertTrue(entry.getValue().equals("81.1496"));
            }
        }

        assertThat("City key should exist", addMap, hasKey("city"));
        assertThat("Geo map must contain both lat & lng", geoMap, allOf(hasKey("lat"), hasKey("lng")));
        assertThat(addMap.get("city").toString(), equalTo("Gwenborough"));
        assertThat(geoMap.get("lat").toString(), equalTo("-37.3159"));
        assertThat(geoMap.get("lng").toString(), equalTo("81.1496"));
    }
}
