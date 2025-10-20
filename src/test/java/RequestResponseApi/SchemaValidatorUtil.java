package RequestResponseApi;

import io.restassured.module.jsv.JsonSchemaValidator;
import java.io.File;

public class SchemaValidatorUtil {

    // 🔹 Validate using static JSON schema file
    public static JsonSchemaValidator validateUsingFile(String relativePath) {
        File schemaFile = new File(System.getProperty("user.dir") + "/src/test/resources/" + relativePath);
        return JsonSchemaValidator.matchesJsonSchema(schemaFile);
    }

    // 🔹 Validate using dynamic schema (inline JSON string)
    public static JsonSchemaValidator validateUsingString(String schemaString) {
        return JsonSchemaValidator.matchesJsonSchema(schemaString);
    }
}