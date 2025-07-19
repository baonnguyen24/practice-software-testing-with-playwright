Feature: Product Catalog
  As a customer,
  I want to easily search, filter, and sort products in the catalog
  So that I can find what I need quickly

  Sally is an online customer shopper

  Rule:Customers should be able to search for products by name
    Example: The one where Sally searches for an Adjustable Wrench
      Given Sally is on the home page
      When she searches for "Adjustable Wrench"
      Then the "Adjustable Wrench" product should be displayed


    Example: The one where Sally searches for a more general term
      Given Sally is on the home page
      When she searches for "saw"
      Then the following products should be displayed:
        | Wood Saw |
        | Circular Saw |

  Rule: Customers should be able to sort products by various criteria
    Scenario Outline: Sally sorts by different criteria
      Given Sally is on the home page
      When she sorts by "<Sort>"
      Then the first product displayed should be "<First Product>"
      Examples:
        | Sort               | First Product        |
        | Name (A - Z)       | Adjustable Wrench    |
        | Name (Z - A)       | Wood Saw             |
        | Price (High - Low) | Drawer Tool Cabinet  |
        | Price (Low - High) | Washers              |