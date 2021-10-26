@test 
Feature: TC-Scenario Outline Parallel

@parallelTestNG @Excel 
Scenario Outline: TC-07-Cogmento web application testing using excel as data input 
	Given I have logged into "Cogmento" application 
	And I navigate to contact page 
	And I enter 'first_name' and 'birth_date' to a create contact 
	When I click on save button 
	Then I should successfully able to see the contact created 
	And I logout from the application
	
	Examples:
	|first_name|birth_date|
	|Mark|2|
	|Rohit|3|
	#|Hastings|4|
	#|Peter|5|
	#|John|6|