package RequestResponseApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SerializationTest {

    @Test
    public void objectMapperSerialization() throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        User user=new User("Praveen","Automation Tester");
        String jsonString=objectMapper.writeValueAsString(user);
        System.out.println(jsonString);
    }

    @Test
    public void objectMapperDeserialization() throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        User user=new User("Praveen","Senior Automation Tester");
        String jsonString=objectMapper.writeValueAsString(user);

        User res=objectMapper.readValue(jsonString, User.class);
        String name=res.getName();
        String job=res.getJob();

        System.out.println(name);
        System.out.println(job);

        assertThat(res.getName(),equalTo("Praveen"));
        assertThat(res.getJob(),equalTo("Senior Automation Tester"));
    }

    /*
    💻 Your Steps
	1.	Create a User Object
	•	Create an instance of User with your values (e.g., "Kesava", "API Automation Engineer").
	2.	Send POST Request
	•	Use Rest Assured .body(user) directly.
	•	API: https://reqres.in/api/users
	•	Validate statusCode = 201
	3.	Extract Response
	•	Check that the response contains:
	•	name
	•	job
	•	id
	•	createdAt
	4.	Assertions
	•	Validate that your input values (like name/job) match response values.
	•	Check that id and createdAt are not null.
     */

    @Test
    public void reqResPostWithPojo()
    {
        User user=new User("Kesava","API Automation Engineer");

        ObjectMapper objectMapper=new ObjectMapper();

        Response response =RestAssured.given().spec(SpecBuilderUtil.getRequestSpecBuilder())
                .body(user)
                .when().post("/api/users")
                .then().assertThat()
                .log().body()
                .statusCode(201).extract().response();

        List<String> expectedKeys= Arrays.asList("name","job","id","createdAt");

        Map<String,Object> resMap=response.jsonPath().getMap("$");
        List<String> actualKeys=new ArrayList<>();
        for(Map.Entry<String,Object> entry:resMap.entrySet())
        {
            actualKeys.add(entry.getKey());
        }

        Assert.assertEquals(actualKeys,expectedKeys);

        assertThat(response.jsonPath().get("name"),equalTo("Kesava"));
        assertThat(response.jsonPath().get("id"),notNullValue());
        assertThat(response.jsonPath().get("createdAt"),notNullValue());
    }

    /*
    🧱 Steps to Implement
	1.	Create a New POJO — UserResponse
	•	Fields → name, job, id, createdAt
	•	Add getters, setters, and a toString() for debug prints.
	2.	Send a POST Request
	•	Use the same User object as request body.
	3.	Deserialize Response
	•	Use .as(UserResponse.class) to map the full JSON into your response POJO.
	4.	Assertions
	•	Validate:
	•	response.getName() matches the request name
	•	response.getJob() matches the request job
	•	id and createdAt are not null
     */

    @Test
    public void deserializePojoMapperTest()
    {
        User user=new User("Kesava","API Automation Engineer");

        ObjectMapper objectMapper=new ObjectMapper();

        UserResponse userResponse=RestAssured.given().spec(SpecBuilderUtil.getRequestSpecBuilder())
                .body(user)
                .when().post("/api/users")
                .then().extract().as(UserResponse.class);

        assertThat(userResponse.getName(),equalTo("Kesava"));
        assertThat(userResponse.getId(),notNullValue());
        assertThat(userResponse.getCreatedAt(),notNullValue());

        System.out.println(userResponse.toString());
    }

    /*
            🧱 Steps to Implement

        1️⃣ Create Address POJO
            •	Fields → city, zip
            •	Add constructor, getters, setters, and toString()

        2️⃣ Create UserDetails POJO
            •	Fields → name, email, address (of type Address)
            •	Add constructor, getters, setters, and toString()

        3️⃣ Inside your test:
            •	Create an Address object
            •	Create a UserDetails object containing that address
            •	Pass UserDetails as body in a POST request to
        👉 https://reqres.in/api/users

        4️⃣ Assertions:
            •	Status = 201
            •	Response contains id and createdAt
            •	Validate name and email returned correctly

     */
    @Test
    public void nestedPojoSerializationTest()
    {
        Address address=new Address("Chennai","600001");
        UserDetails userDetails=new UserDetails("Kesava","kesava@test.com",address);

        RestAssured.given().spec(SpecBuilderUtil.getRequestSpecBuilder())
                .body(userDetails)
                .when().post("/api/users")
                .then().assertThat().statusCode(201)
                .body("$",hasKey("id"))
                .body("name",equalTo("Kesava"))
                .body("email",equalTo("kesava@test.com"));

    }

    /*
                🧱 Steps to Implement
            1️⃣ Create an ObjectMapper instance.
            2️⃣ Use ObjectMapper.createObjectNode() → to create a root object.
            3️⃣ Add primitive fields using put() (like name, email).
            4️⃣ Add an array using ArrayNode → e.g., skills.
            5️⃣ Add a nested object for experience → another ObjectNode.
            6️⃣ Send it as the body in a POST request to
            👉 https://reqres.in/api/users
            7️⃣ Validate:
                •	Status = 201
                •	Body contains “id” and “createdAt”
     */

    @Test
    public void dynamicJsonWithObjectMapper()
    {
        ObjectMapper objectMapper=new ObjectMapper();
        ObjectNode root=objectMapper.createObjectNode();
        root.put("name","Kesava");
        root.put("email","kesava@test.com");

        ArrayNode skills=objectMapper.createArrayNode();
        skills.add("API Testing");
        skills.add("Java");
        skills.add("Postman");
        root.set("skills",skills);

        ObjectNode exp=objectMapper.createObjectNode();
        exp.put("years",3);
        exp.put("domain","Banking");
        root.set("experience",exp);

        RestAssured.given().spec(SpecBuilderUtil.getRequestSpecBuilder())
                .body(root)
                .when().post("/api/users")
                .then().assertThat().statusCode(201)
                .log().body()
                .body("$",hasKey("id"))
                .body("$",hasKey("createdAt"));
    }
}
