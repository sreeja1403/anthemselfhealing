@selfhealing
Feature: Automation practice and OrangeHRM validation

@testsh1 @Excel 
Scenario: TC-01-User validation in OrangeHRM web application
	Given I have logged into "OrangeHRM" application 
	And I navigate to system users page 
	When I click on "Admin" user link 
	Then I should successfully able to see user details 
	And I logout from the Cogmento application		
	
@test21 @Excel 
  Scenario: TC-01-Validating personal information in Automation practice 
    Given I have logged into "Automation Practice" application 
     When I navigate to personal information page 
     Then I validate personal details in personal information page: 
     
      | Social title | First name | Last name  | E-mail address                     | dobDay | dobMonth | dobYear | 
      | Mr.          | Abhilash   | Giridharan | abhilash.giridharan@ust-global.com | 8      | November | 1987    | 

@testsh2  
Scenario: TC-01-Radio button check in MyApp 
	Given I have logged into "MyApp" application 
	Then I perform the control operations in MyApp
	

@testsh1 @Excel 
Scenario: TC-02-User check in OrangeHRM application
	Given I have logged into "OrangeHRM" application 
	And I navigate to system users page 
	When I click on "Admin" user link 
	Then I should successfully able to see user details 
	And I logout from the Cogmento application	
	
@test21 @Excel 
  Scenario: TC-02-Automation practice user validation
    Given I have logged into "Automation Practice" application 
     When I navigate to personal information page 
     Then I validate personal details in personal information page: 
      | Social title | First name | Last name  | E-mail address                     | dobDay | dobMonth | dobYear | 
      | Mr.          | Abhilash   | Giridharan | abhilash.giridharan@ust-global.com | 8      | November | 1987    | 
@testsh2  
Scenario: TC-02-MyApp  application 
	Given I have logged into "MyApp" application 
	Then I perform the control operations in MyApp
	

@testsh1 @Excel 
Scenario: TC-03-OrangeHRM web application validation
	Given I have logged into "OrangeHRM" application 
	And I navigate to system users page 
	When I click on "Admin" user link 
	Then I should successfully able to see user details 
	And I logout from the Cogmento application	
		
@test21 @Excel 
 Scenario: TC-03-AP Validation
    Given I have logged into "Automation Practice" application 
     When I navigate to personal information page 
     Then I validate personal details in personal information page: 
      | Social title | First name | Last name  | E-mail address                     | dobDay | dobMonth | dobYear | 
     | Mr.          | Abhilash   | Giridharan | abhilash.giridharan@ust-global.com | 8      | November | 1987    | 
 
Scenario: TC-03-Self MyApp validation 
	Given I have logged into "MyApp" application 
	Then I perform the control operations in MyApp
	 


