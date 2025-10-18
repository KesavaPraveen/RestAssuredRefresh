package RequestResponseApi;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class SpecBuilderUtil {

    public static RequestSpecification getRequestSpecBuilder()
    {
        RequestSpecification requestSpecification=
                new RequestSpecBuilder()
                        .setBaseUri(ConfigReader.initiateProp().getProperty("baseUri"))
                        .addHeader("x-api-key", "reqres-free-v1")
                        .setContentType(ConfigReader.initiateProp().getProperty("contentType"))
                        .build();
        return requestSpecification;
    }

    public static ResponseSpecification getResponseSpecBuilder(int statusCode)
    {
        ResponseSpecification responseSpecification
                =new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .log(LogDetail.BODY)
                .build();
        return responseSpecification;
    }
}
