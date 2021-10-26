@paralleltestngg
Feature: Automation practice validation


@test21 @Excel 
  Scenario Outline: TC-01-Validating personal information in Automation practice 
    Given I  logged into "AutomationPractice1" application 
     When I navigate to personal information page 
     Then I validate personal details in personal information page: 
     
     Examples:
      | Social title | First name | Last name  | E-mail address                     | dobDay | dobMonth | dobYear | 
      | Mr.          | Abhilash   | Giridharan | abhilash.giridharan@ust-global.com | 8      | November | 1987    | 
      
#@test21 @Excel 
#  Scenario Outline: TC-01-Validating personal information in Automation practice 
#    Given I  logged into "AutomationPractice2" application 
#     When I navigate to personal information page 
#     Then I validate personal details in personal information page: 
#     
#     Examples:
#      | Social title | First name | Last name  | E-mail address                     | dobDay | dobMonth | dobYear | 
#      | Mr.          | Abhilash   | Giridharan | abhilash.giridharan@ust-global.com | 8      | November | 1987    | 



