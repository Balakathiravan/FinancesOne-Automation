package api;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class ApiBase {

    @BeforeClass
    public void setupApi() {
        RestAssured.baseURI = "https://api.worldbank.org/v2";
    }
}
