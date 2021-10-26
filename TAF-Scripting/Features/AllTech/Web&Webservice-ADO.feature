@ADO
Feature: Testing Webservices, Web applications 

@Api @USTI-22063
Scenario: Sample GET method API test without parameters 
	Given I have sample Get API 
	When I send a "Get" Request with "sampleGETApi" properties 
	Then I should get response code 200 
	And the response should contain: 
		| JsonPath		  | ExpectedVal														   |
		| data.id         | 2                                                                  | 
		| data.email      | janet.weaver@reqres.in                                             | 
		| data.first_name | Janet                                                              | 
		| data.last_name  | Weaver                                                             | 
		| data.avatar     | https://s3.amazonaws.com/uifaces/faces/twitter/josephstein/128.jpg |
		
@Web @USTI-22064
Scenario: TC-01-Validating perosnal information in Automation practice 
	Given I have logged into "Cogmento" application 
	And I navigate to contact page 
	And I enter personal details to a create contact 
	When I click on save button 
	Then I should successfully able to see the contact created 
	And I should be able to delete contact added 
	And I logout from the application 