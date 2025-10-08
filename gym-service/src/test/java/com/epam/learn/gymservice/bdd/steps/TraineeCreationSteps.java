package com.epam.learn.gymservice.bdd.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

public class TraineeCreationSteps {

  @LocalServerPort private int port;

  private String payload;
  private Response response;

  @Before
  public void setUp() {
    RestAssured.port = port;
  }

  @Given("a new trainee JSON payload:")
  public void a_new_trainee_json_payload(String payload) {
    this.payload = payload;
  }

  @When("I send a POST request to {string}")
  public void i_send_a_post_request_to(String endpoint) {
    System.out.println(RestAssured.port);
    response =
        RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(payload)
            .when()
            .post(endpoint);
  }

  @Then("the response status code should be {int}")
  public void the_response_status_code_should_be(int statusCode) {
    System.out.println(statusCode);
    response.then().statusCode(statusCode);
  }
}
