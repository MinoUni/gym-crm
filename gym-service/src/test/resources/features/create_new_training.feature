Feature: Create new Training
  As a trainer
  I want to create a new training session for a trainee
  So that the trainee can follow the training program

  Scenario Outline: Create new training session with various payloads
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
    Then the response status code should be <statusCode>

    Examples:
      | name | date       | duration | trainingTypeId | traineeUsername | trainerUsername | statusCode |
      |      | 2025-10-21 | 00:55:00 | 1              | Aki.Shroom      | Kevin.Kaslana   | 400        |
      | TR-2 | 2025-08-22 | 00:45:00 | 1              | Aki.Shroom      | Kevin.Kaslana   | 400        |
      | TR-3 | 2025-10-23 | 00:35:00 | 1              | Aki.Shroom      | Kevin.Kaslana   | 204        |
