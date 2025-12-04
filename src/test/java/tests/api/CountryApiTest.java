package tests.api;

import api.ApiBase;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;


public class CountryApiTest extends ApiBase {

    @Test(priority = 2)
    public void countryListApi200() {
        Response res = given()
                .queryParam("format","json")
                .get("/country");
        Reporter.getCurrentTestResult().setAttribute("apiResponse", res.getBody());
        Assert.assertEquals(res.statusCode(), 200, "Status code for country list is not 200");
    }

    @Test(priority = 2)
    public void indiaDetailsApi() {
        Response res = given()
                .queryParam("format","json")
                .get("/country/IND");
        Reporter.getCurrentTestResult().setAttribute("apiResponse", res.getBody());
        Assert.assertEquals(res.statusCode(),200, "Status code for IND is not 200");
        Assert.assertEquals(res.jsonPath().getString("[1][0].id"),"IND", "Country id is not IND");
    }
}
