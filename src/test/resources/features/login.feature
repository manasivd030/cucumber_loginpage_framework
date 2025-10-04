Feature: Login

  @smoke
  Scenario: Successful login with valid credentials
    Given I open the login page
    When I login with valid credentials
    Then I should see a success message "You logged into a secure area!"

  Scenario: Unsuccessful login with invalid credentials
    Given I open the login page
    When I login with username "tomsmith" and password "WrongPassword"
    Then I should see an error message "Your password is invalid!"
