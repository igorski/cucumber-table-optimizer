Feature: Example Cucumber feature file #2

  @tag3

  Scenario Outline: qux baz bar foo
    Given I have a foo with "<property1>" with "<property2>"
    When I bar it
    Then The baz will be quxing
    When I add clipart

    Examples:
      | property1 | property2 |
      | qux       | quux      |
      | baz       | qux       |
      | bar       | baz       |
      | foo       | bar       |
