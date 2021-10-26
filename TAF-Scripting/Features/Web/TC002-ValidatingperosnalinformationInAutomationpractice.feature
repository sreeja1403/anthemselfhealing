@test12
Feature: TC-Validating perosnal information in Automation practice 

@test21 @Excel 
  Scenario: TC-01-Validating perosnal information in Automation practice 
    Given I have logged into "Automation Practice" application 
     When I navigate to personal information page 
     Then I validate personal details in personal information page: 
      | Social title | First name | Last name  | E-mail address                     | dobDay | dobMonth | dobYear | 
      | Mr.          | Abhilash   | Giridharan | abhilash.giridharan@ust-global.com | 8      | November | 1987    | 

 @test22 @Excel @Negative 
  Scenario: TC-01-Validating perosnal information in Automation practice 
    Given I have logged into "Automation Practice" application 
     When I navigate to personal information page 
     Then I validate personal details in personal information page: 
      | Social title | First name | Last name  | E-mail address                     | dobDay | dobMonth | dobYear | 
      | Mr.          | Abhilash   | Giri | abhilash.giridharan@ust-global.com | 8      | November | 1987    | 
      