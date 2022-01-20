Feature: API Tests
  Scenario: GET request to /users
  Given existing Server application https://reqres.in/api
  Then on GET request to /users it returns expected users list

  Scenario: GET request to single user
  Given existing Server application https://reqres.in/api
  Then on GET request to /users/2 it returns expected welcome message

  Scenario: GET request to nonexisting user
  Given existing Server application https://reqres.in/api
  Then on GET request to /users/23 it returns 404 status code