@jenkinstest 
Feature: Demo web application radio button click 

@test1 @Excel
Scenario: TC-01-Click on a radio button
	Given I have logged into "Demo" application 
	And I click on radio button Radio1
	
@test2 @Excel
Scenario: TC-02-Click on a checkbox
	Given I have logged into "Demo" application 
	And I click on check box Option1
	
@test3 @Excel 
Scenario: TC-02-Enter text into suggestion box
	Given I have logged into "Demo" application 
	And I enter text into suggestion class