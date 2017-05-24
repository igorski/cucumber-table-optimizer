Feature: Example Cucumber feture file

  @tag1 @tag2

  Scenario Outline: foo bar baz qux
    Given I have a foo with "<property1>" with "<property2>"
    When I bar it
    Then The baz will be quxing
    When I add clipart

    Examples:
      | property1 | property2 |
      | foo       | bar       |
      | bar       | baz       |
      | qux       | quux      |
      | quux      | quuz      |
      | quuz      | corge     |
      | corge     | grault    |
