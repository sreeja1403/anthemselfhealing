@parallelTestNG12
Feature: UTS_IRCTC

@test1 @AndroidNative
Scenario: Search and Book Ticket
    Given I have IRCTC native application 
   	And I search for tickets without logging
	And I select the train from the search list

    