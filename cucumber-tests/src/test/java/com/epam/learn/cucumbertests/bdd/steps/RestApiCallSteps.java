package com.epam.learn.cucumbertests.bdd.steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class RestApiCallSteps {

  private static final String JWT_TOKEN =
      "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvRVc0QTg3aXhNay0tN21DWmhHcGNXcG1hcFMwa3FwRTJEMlAwaWdRN3dzIn0.eyJleHAiOjE3NjA0NzIyMTYsImlhdCI6MTc2MDQ3MTkxNiwianRpIjoib25ydHJvOjdjZTU2MmE4LTQyODMtNWE4MS01YjA1LWU1NmY3MzNjNjBhYiIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvZ3ltY3JtIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjhjMDIyODdjLTM0NmYtNGYyMy05YTE3LThmNDU2ODQxZjY5NiIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFwaS1nYXRld2F5Iiwic2lkIjoiZWQ0ZWQ0MjMtNmUxMC00YWUwLWFlNjgtZTBmZTUzYzE1NjhjIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLWd5bWNybSIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJVU0VSIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiQWtpIFNocm9vbSIsInByZWZlcnJlZF91c2VybmFtZSI6ImFraS5zaHJvb20iLCJnaXZlbl9uYW1lIjoiQWtpIiwiZmFtaWx5X25hbWUiOiJTaHJvb20iLCJlbWFpbCI6ImFraS5zaHJvb21AbWFpbC5jb20ifQ.EARtXRV1CGF5wMISTTxxaMAbWyvGKfs1hNs2IqyHhdWVeh_XgjKBjUsIgMpeYbDJx_ZtOhW_Xu44qJP31FTA3NTNVgArWOm-6pOu_aS6V1P9mbxNIdf_VqgLiC8uGpUB2HOEnk0n9VLz5OJEK_i5VdBc1kbh_FugEMULGn4zqnQo6vXdH92UO8lfC8mtbpCrTRpuNSZOqI0dqAJPu3LkbcOopdCNQjq34mtZ3Yq2D8Jygnqs9kdQ6_Jze3cSK_BEc4XFH8uk9JHB-mXTYYfQ5uSStPTXZUrb0RmmwaXY8WwcNo9K4tC-bFEpHnZn70jvc-k3LM42GsjgpNdAbBne8g";
  private static final int GYM_SERVICE_PORT = 8083;
  private static final int TRAINING_SESSION_SERVICE_PORT = 8082;

  private String payload;
  private Response gymServiceResponse;

  @Given("a JSON payload:")
  public void a_json_payload(String payload) {
    this.payload = payload;
  }

  @When("I send a POST request to {string}")
  public void i_send_a_post_request_to(String endpoint) {
    gymServiceResponse =
        RestAssured.given()
            .log()
            .all()
            .port(GYM_SERVICE_PORT)
            .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(payload)
            .when()
            .post(endpoint);
  }

  @Then(
      "gym-service send event to the training-session-service and respond with status code: {int}")
  public void gymServiceSendEventToTheTrainingSessionServiceAndRespondWithStatusCode(
      int statusCode) {
    gymServiceResponse.then().log().all().statusCode(statusCode);
  }

  @Then("gym-service response status code should be {int} and code {string}")
  public void gymServiceResponseStatusCodeShouldBeAndCode(int expectedStatusCode, String code) {
    gymServiceResponse
        .then()
        .assertThat()
        .statusCode(expectedStatusCode)
        .and()
        .body("code", equalTo(code));
  }

  @And("a training-session-service receive an event to create a new training session for {string}")
  public void trainingSessionServiceReceiveAnEventToCreateANewTrainingSessionForTrainer(
      String trainerUsername) {
    RestAssured.given()
        .log()
        .all()
        .port(TRAINING_SESSION_SERVICE_PORT)
        .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN)
        .pathParam("trainerUsername", trainerUsername)
        .when()
        .get("api/v1/training-sessions/{trainerUsername}")
        .then()
        .log()
        .all()
        .assertThat()
        .statusCode(200)
        .body("trainerUsername", is(trainerUsername))
        .and()
        .body("workloadSummary.size()", is(1));
  }

  @And("training-session-service shouldn't create any new training session for {string}")
  public void trainingSessionServiceShouldnTCreateAnyNewTrainingSessionFor(String trainerUsername) {
    RestAssured.given()
        .log()
        .all()
        .port(TRAINING_SESSION_SERVICE_PORT)
        .header(HttpHeaders.AUTHORIZATION, JWT_TOKEN)
        .pathParam("trainerUsername", trainerUsername)
        .when()
        .get("api/v1/training-sessions/{trainerUsername}")
        .then()
        .log()
        .all()
        .assertThat()
        .statusCode(404);
  }
}
