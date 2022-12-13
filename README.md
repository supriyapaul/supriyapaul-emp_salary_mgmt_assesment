# supriyapaul-emp_salary_mgmt_assesment

Design Decisions:
-----------------
A. Steps to Build & Run the Application:
---------------------------------------
  1. Enable Annotation Processing Checkbox in the IDE(Eclipse/Intellij) to work with Lombok.
  2. Set SDK to 1.8.
  3. Execute maven Goal Clean install to run the test cases build the application.
  4. Run main Application(SalaryManagementApplication.java) to start the server.

B. Exception Handling:
---------------------
  1. Exceptions which are regarding Rules/Constraints Validation are handled under ValidationException class.
  2. Exceptions which are regarding Application Specific are handled under UserException class.
  3. All other Exceptions are handled under Exception class.
  4. Treatment for All these type of exceptions are given in a controller advice(annotated with @ControllerAdvice) with standard
     response format with different type status codes.

C. Validation Framework:
------------------------
   1. All input request dto's(required validation) are extended from class AbstractRequest Class.
   2. Created Generic interface ValidationService of Type AbstractRequest Class Type. It has two Default methods in order to validate Request/
      Requests.
   3. Created Generic interface DefaultValidationService of Type BaseValidation Class Type. It has two Default methods in order to validate Request/
      Requests which are common/default to all requests of specific type(user(employee)/search related) of API's.
   4.Created Abstract Classes(AbstractUserValidationService and AbstractSearchRequestValidationService) which implements this DefaultValidationService
      interface to handle common validation specific to user or search type requests.
   5.Created Implementation classes which extends/implements from these abstract classes/interfaces to handle both common/default or specific
      validations.
   6. Implemented Factory Design pattern (ValidationFactory) in order to instantiate beans for these implementation classes and add those beans
      in Spring context at runtime.(Lazy Initialization)
   7. Created a DTO class(ValidationRule) which has multiple fields to provide Rule Name, Field Name , Operation Name etc. in order to work
      with this validation framework.


D. Filters:
-----------
    1. Created a DTO class(SearchFilter) in order to provide Filter related attributes.
    2. Created a Class(UserSpecification) which implements Spring JPA Specificatio in order to handle Filtering dynamically.

E. Request & Response Handling:
------------------------------
    1. Standart Base Request DTO format created. All Request DTO class must extends this base request DTO.
    2. Standart Response format created.

F. Suggestion for improvement:
-----------------------------
    1. Validation Framework can be enhanced with more operations to make it more generic.
    2. More Filters can be added to make more efficient.
    3. We can provide Validation data in  XML based configuration in order to decouple this from build pacakge so that change in XML configuration will
       not trigger new build to incorporate the changes.