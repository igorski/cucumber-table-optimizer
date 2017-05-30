Feature: Example Cucumber feature file

  @tag1 @tag2

  Scenario Outline: foo bar baz qux quux quuz corge
    Given I have "<property1>" with "<property2>"
    When I bar it
    Then The baz will be quxing

    Examples:
      | property1 | property2 |
      | foo       | bar       |
      | bar       | baz       |

  @tag3
    Examples:
      | property1 | property2 |
      | baz       | qux       |
      | qux       | quux      |
      #| quuz      | quuz      |
