@test 
Feature: TC-Cogmento web application testing by fetching data from excel and Genrocket 

@test11 @Excel
Scenario: TC-01-Cogmento web application testing using excel as data input 
	Given I have logged into "Cogmento" application 
	And I navigate to contact page 
	And I enter personal details to a create contact 
	When I click on save button 
	Then I should successfully able to see the contact created 
	And I should be able to delete contact added 
	And I logout from the application 
	
@test12 @Genrocket 
Scenario: TC-02-Cogmento web application testing using GenRocket as data input 
	Given I have logged into "Cogmento" application 
	And I navigate to contact page 
	And I enter personal details to a create contact 
	When I click on save button 
	Then I should successfully able to see the contact created 
	And I should be able to delete contact added 
	And I logout from the application