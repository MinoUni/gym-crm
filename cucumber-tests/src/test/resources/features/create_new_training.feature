Feature: Create new Training
  As a trainer
  I want to create a new training session for a trainee
  So that the trainee can follow the training program

  Scenario Outline: Successful creation of a new training session
    Given a JSON payload:
        """
        {
          "name": "<name>",
          "date": "<date>",
          "duration": "<duration>",
          "trainingTypeId": <trainingTypeId>,
          "traineeUsername": "<traineeUsername>",
          "trainerUsername": "<trainerUsername>"
        }
        """
    When I send a POST request to "/trainings"
    Then gym-service send event to the training-session-service and respond with status code: <statusCode>
    And a training-session-service receive an event to create a new training session for "<trainerUsername>"

    Examples:
      | name | date       | duration | trainingTypeId | traineeUsername | trainerUsername | statusCode |
      | TR-1 | 2025-10-23 | 00:35:00 | 1              | Aki.Shroom      | Kevin.Kaslana   | 204        |

  Scenario Outline: Unsuccessful creation of a new training session due to invalid payload
    Given a JSON payload:
        """
        {
          "name": "<name>",
          "date": "<date>",
          "duration": "<duration>",
          "trainingTypeId": <trainingTypeId>,
          "traineeUsername": "<traineeUsername>",
          "trainerUsername": "<trainerUsername>"
        }
        """
    When I send a POST request to "/trainings"
    Then gym-service response status code should be <statusCode> and code "<code>"
    And training-session-service shouldn't create any new training session for "<trainerUsername>"

    Examples:
      | name | date       | duration | trainingTypeId | traineeUsername | trainerUsername | statusCode | code              |
      |      | 2022-10-21 | 00:55:00 | -1             | Aki.Shroom      | Kervin.Smith    | 400        | VALIDATION_FAILED |
      | TR-3 | 2025-12-22 | 00:45:00 | 1              | None.Name       | Josh.Imp        | 404        | NOT_FOUND         |
