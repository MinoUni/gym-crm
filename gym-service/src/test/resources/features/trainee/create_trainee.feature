Feature: Trainee Creation
  As a new user of the Gym Management System
  I want to create a trainee profile
  So that I can access training programs and track my progress

  Scenario Outline: Trainee Creation with various payloads
    Given a new trainee JSON payload:
    """
    {
      "firstName": "<firstName>",
      "lastName": "<lastName>",
      "birthDate": "<birthDate>",
      "address": "<address>"
    }
    """
    When I send a POST request to "/trainees"
    Then the response status code should be <statusCode>

    Examples:
      | firstName | lastName | birthDate  | address     | statusCode |
      | John      | Doe      | 1990-01-01 | 123 Main St | 201        |
      | Kate      | Thomson  | 1990-01-01 |             | 201        |
      | Jane      | Smith    | 2026-05-15 | 456 Oak Ave | 400        |
      | Alice     |          | 1992-07-20 | 789 Pine Rd | 400        |
      |           | Wong     | 1992-07-20 | 789 Pine Rd | 400        |
