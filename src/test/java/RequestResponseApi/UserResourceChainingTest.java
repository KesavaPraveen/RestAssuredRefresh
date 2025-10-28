package RequestResponseApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

public class UserResourceChainingTest extends BaseApiTest{

    public static int userId;
    @Test
    public void createUserTest()
    {
        User user=new User("Kesava","Senior API Automation Tester");

        Response response =RestAssured.given().spec(SpecBuilderUtil.getRequestSpecBuilder())
                .body(user)
                .when().post("/api/users")
                .then()
                .spec(SpecBuilderUtil.getResponseSpecBuilder(201))
                .extract().response();

        userId=response.jsonPath().getInt("id");
        System.out.println("Id: " +userId);
        Map<String,Object> resMap=response.jsonPath().getMap("$");
        Assert.assertTrue(resMap.containsKey("id"));
        Assert.assertTrue(resMap.containsKey("name"));
        Assert.assertTrue(resMap.containsKey("createdAt"));

        Assert.assertNotNull(resMap.get("id"));
        Assert.assertEquals(user.getName(),resMap.get("name"));
        Assert.assertEquals(user.getJob(),resMap.get("job"));
        assertThat(resMap.get("id"),notNullValue());
    }

    @Test
    public void getUserDetailsTest()
    {
        Response response=RestAssured.given().spec(SpecBuilderUtil.getRequestSpecBuilder())
                .pathParam("userId",2)
                .when().get("/api/users/{userId}")
                .then()
                .spec(SpecBuilderUtil.getResponseSpecBuilder(200))
                .extract().response();

        int id=response.jsonPath().getInt("data.id");

        assertThat(response.jsonPath().get("data"),hasKey("email"));
        assertThat(response.jsonPath().get("data"),hasKey("first_name"));
        assertThat(response.jsonPath().get("data"),hasKey("last_name"));
        assertThat(response.jsonPath().get("data"),hasKey("avatar"));
    }

    @Test
    public void updatedUserDetailsTest() throws JsonProcessingException {
        User user=new User("Kesava Praveen","Lead API Tester");
        ObjectMapper objectMapper=new ObjectMapper();
        String resPayload=RestAssured.given().spec(SpecBuilderUtil.getRequestSpecBuilder())
                .pathParam("userId",2)
                .body(user)
                .when().put("/api/users/{userId}")
                .then()
                .spec(SpecBuilderUtil.getResponseSpecBuilder(200))
                .extract().asString();

        UserResponseUpdated deserialize=objectMapper.readValue(resPayload,UserResponseUpdated.class);
        Assert.assertEquals(user.getName(),deserialize.getName());
        Assert.assertEquals(user.getJob(),deserialize.getJob());
        Assert.assertNotNull(deserialize.getUpdatedAt());
        String updatedAt= deserialize.getUpdatedAt();
        System.out.println("Updated At: " +updatedAt);
    }

    @Test
    public void deleteUserDetailsTest()
    {
        String resPayload=RestAssured.given().spec(SpecBuilderUtil.getRequestSpecBuilder())
                .pathParam("userId",2)
                .when().delete("/api/users/{userId}")
                .then()
                .spec(SpecBuilderUtil.getResponseSpecBuilder(204))
                .extract().asString();

        Assert.assertTrue(resPayload.isEmpty());
    }

    @Test
    public void getDeletedUserDetailTest()
    {
        RestAssured.given().spec(SpecBuilderUtil.getRequestSpecBuilder())
                .pathParam("userId",2)
                .when().get("/api/users/{userId}")
                .then()
                .spec(SpecBuilderUtil.getResponseSpecBuilder(404));
    }
}
