package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class StepDefinitionAPI {
    RequestSpecification request =  null;
    Response response = null;

    @Given("existing Server application https:\\/\\/reqres.in\\/api")
    public void existing_server_application_https_reqres_in_api() {
    request = RestAssured.given();
    request.baseUri("https://reqres.in/api");
    request.header("Content-Type","application/json");
    }

    @Then("on GET request to \\/users it returns expected users list")
    public void on_get_request_to_users_it_returns_expected_users_list() {
        response = request.get("/users");
        System.out.println(response.getBody().prettyPrint());

        String page = getStringFromJson(response.asString(),"$.page");
        String perPage = getStringFromJson(response.asString(),"$.per_page");
        String total = getStringFromJson(response.asString(),"$.total");
        String totalPages = getStringFromJson(response.asString(),"$.total_pages");
        String firstID = getStringFromJson(response.asString(),"$.data[0].id");
        String firstEmail = getStringFromJson(response.asString(),"$.data[0].email");
        String firstName = getStringFromJson(response.asString(),"$.data[0].first_name");
        String lastName = getStringFromJson(response.asString(),"$.data[0].last_name");
        System.out.println(getJsonNode(response.asString(),"$.data"));

        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertEquals(page,"1");
        Assert.assertEquals(perPage,"6");
        Assert.assertEquals(total,"12");
        Assert.assertEquals(totalPages,"2");
        Assert.assertEquals(firstID,"1");
        Assert.assertEquals(firstEmail,"george.bluth@reqres.in");
        Assert.assertEquals(firstName,"George");
        Assert.assertEquals(lastName,"Bluth");
    }


    @Then("on GET request to \\/users\\/{int} it returns expected welcome message")
    public void on_get_request_to_users_it_returns_expected_welcome_message(Integer userID) {
        response = request.get("/users/"+userID);
        System.out.println(response.getBody().prettyPrint());

        String email = getStringFromJson(response.asString(),"$.data.email");
        String firstName = getStringFromJson(response.asString(),"$.data.first_name");
        String lastName = getStringFromJson(response.asString(),"$.data.last_name");

        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertEquals(email,"janet.weaver@reqres.in");
        Assert.assertEquals(firstName,"Janet");
        Assert.assertEquals(lastName,"Weaver");

    }

    @Then("on GET request to \\/users\\/{int} it returns {int} status code")
    public void on_get_request_to_users_it_returns_status_code(Integer userID, int expectedStatusCode) {
        response = request.get("/users/"+userID);
        System.out.println(response.getBody().prettyPrint());
        Assert.assertEquals(response.getStatusCode(),expectedStatusCode);
    }

    public static Object getJsonNode(String json, String path)
    {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        Object data = JsonPath.read(document, path);
        return data;
    }

    public static String getStringFromJson(String json, String path)
    {
        Object jsonNode = getJsonNode(json,path);
        String data = jsonNode.toString();
        System.out.println("Class Name "+jsonNode.getClass());
        return data;
    }
}