package core;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Before;

public class BaseTest {

    @Before
    public void setup(){

        RestAssured.baseURI = "http://barrigarest.wcaquino.me";
        RestAssured.port = 80;

        RequestSpecBuilder requestBuilder = new RequestSpecBuilder();
        requestBuilder.setContentType(ContentType.JSON);
        RestAssured.requestSpecification = requestBuilder.build();

        ResponseSpecBuilder responseBuilder = new ResponseSpecBuilder();
        responseBuilder.expectResponseTime(Matchers.lessThan(5000L));
        RestAssured.responseSpecification = responseBuilder.build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }

}
