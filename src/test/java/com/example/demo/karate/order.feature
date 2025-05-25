
Feature: Resilience of Order API under chaos

  Scenario: Get existing order should respond gracefully
    Given url baseUrl + '/orders/123'
    When method GET
    Then status 200
    And match response contains { id: '123', status: '#string' }

  Scenario: Create order should work even during chaos
    Given url baseUrl + '/orders'
    And request { item: 'book', quantity: 2 }
    When method POST
    Then status 200
    And match response contains { id: '#string', status: 'processing' }
