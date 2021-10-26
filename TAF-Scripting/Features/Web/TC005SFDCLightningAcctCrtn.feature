@SFDC 
Feature: TC-SFDC lightening web application testing by fetching data from excel 

@test51 @Excel
Scenario: TC-01-SFDC Lightening application account creation using excel as data input 
	Given I have logged into "SFDC Lightening" application 
	And I navigate to "SFDCAccounts" page 
	When I enter "SFDCAccount" creation details and click save
	Then I should be able to see the "SFDCAccount" created
	
@test52 @Excel
Scenario: TC-02-SFDC Lightening application contact creation using excel as data input 
	Given I have logged into "SFDC Lightening" application 
	And I navigate to "SFDCcontacts" page 
	When I enter "SFDCContact" creation details and click save
	Then I should be able to see the "SFDCContact" created
	
@test53 @Excel
Scenario: TC-03-SFDC Ligthening application opportunities creation using excel as data input 
	Given I have logged into "SFDC Lightening" application 
	And I navigate to "SFDCOpportunities" page 
	When I enter "SFDCOpportunities" creation details and click save
	Then I should be able to see the "SFDCOpportunities" created	
	
@test54 @Excel
Scenario: TC-04-SFDC Ligthening application 
	Given I have logged into "SFDC Lightening" application
	And I navigate to "SFDCContacts" page
	When I enter activites details and click save
	Then I should be able to see the activities listed
	#Then I should be able to see the "SFDCContactactivities" listed