package RequestResponseApi;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class JsonSchemaTest {

    /*
    json schema validation
     */

    @Test
    public void staticJsonSchemaTest() {
        RestAssured.given()
                .when().get("https://reqres.in/api/users?page=2")
                .then()
                .body(JsonSchemaValidator.
                        matchesJsonSchemaInClasspath("schemas/userSchema.json"));

    }

    @Test
    public void dynamicJsonSchemaValidation() {

        String dynamicSchema = "{\n" +
                "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
                "  \"type\": \"object\",\n" +
                "  \"required\": [\"page\", \"data\"],\n" +
                "  \"properties\": {\n" +
                "    \"page\": { \"type\": \"integer\" },\n" +
                "    \"data\": {\n" +
                "      \"type\": \"array\",\n" +
                "      \"items\": {\n" +
                "        \"type\": \"object\",\n" +
                "        \"properties\": {\n" +
                "          \"id\": { \"type\": \"integer\" },\n" +
                "          \"email\": { \"type\": \"string\" }\n" +
                "        },\n" +
                "        \"required\": [\"id\", \"email\"]\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        RestAssured.given()
                .when().get("https://reqres.in/api/users?page=2")
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(dynamicSchema));

    }

    @Test
    public void negativeSchemaValidationTest() {

        // Schema missing the "email" field intentionally
        String invalidSchema = "{\n" +
                "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
                "  \"type\": \"object\",\n" +
                "  \"required\": [\"page\", \"data\"],\n" +
                "  \"properties\": {\n" +
                "    \"page\": { \"type\": \"integer\" },\n" +
                "    \"data\": {\n" +
                "      \"type\": \"array\",\n" +
                "      \"items\": {\n" +
                "        \"type\": \"object\",\n" +
                "        \"properties\": {\n" +
                "          \"id\": { \"type\": \"integer\" }\n" +
                "        },\n" +
                "        \"required\": [\"id\"]\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        System.out.println("🔎 Running negative schema validation...");

        RestAssured.given()
                .when().get("https://reqres.in/api/users?page=2")
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(invalidSchema));

        System.out.println("This line should not be reached (validation must fail)");
    }

    @Test
    public void strictNegativeSchemaValidationTest() {

        String strictSchema = "{\n" +
                "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
                "  \"type\": \"object\",\n" +
                "  \"required\": [\"page\", \"data\"],\n" +
                "  \"properties\": {\n" +
                "    \"page\": { \"type\": \"integer\" },\n" +
                "    \"data\": {\n" +
                "      \"type\": \"array\",\n" +
                "      \"items\": {\n" +
                "        \"type\": \"object\",\n" +
                "        \"properties\": {\n" +
                "          \"id\": { \"type\": \"integer\" }\n" +
                "        },\n" +
                "        \"required\": [\"id\"],\n" +
                "        \"additionalProperties\": false\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"additionalProperties\": false\n" +
                "}";

        System.out.println("🔎 Running strict negative schema validation...");

        RestAssured.given()
                .when().get("https://reqres.in/api/users?page=2")
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(strictSchema));

        System.out.println("❌ Should fail due to extra fields like email, first_name, etc.");
    }
    @Test
    public void staticSchemaValidationWithUtil() {
        RestAssured.given()
                .when().get("https://reqres.in/api/users?page=2")
                .then()
                .assertThat()
                .body(SchemaValidatorUtil.validateUsingFile("schemas/userSchema.json"));
    }

    @Test
    public void dynamicSchemaValidationWithUtil() {

        String schema = "{\n" +
                "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
                "  \"type\": \"object\",\n" +
                "  \"required\": [\"page\", \"data\"],\n" +
                "  \"properties\": {\n" +
                "    \"page\": { \"type\": \"integer\" },\n" +
                "    \"data\": {\n" +
                "      \"type\": \"array\" }\n" +
                "  },\n" +
                "  \"additionalProperties\": true\n" +
                "}";

        RestAssured.given()
                .when().get("https://reqres.in/api/users?page=2")
                .then()
                .assertThat()
                .body(SchemaValidatorUtil.validateUsingString(schema));
    }


}
