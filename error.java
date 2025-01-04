Story Point for GitLab Ticket


---

Title:

Implement a Generic DAO for Logging Errors in a Unified Database Table


---

Description:

Develop a reusable method in the DAO layer to log application errors with the following attributes:

1. Error Type: An enum categorizing errors (e.g., PAYMENT_FAILURE, LOGICAL_FAILURE, etc.).


2. Error Code: A unique integer representing the specific error.


3. Additional Details: A string representation of useful numbers or additional information.


4. Timestamp: The time when the error occurred.



The method should insert error logs into a unified database table and be reusable throughout the application. The table will store details in a structured format to support debugging and analytics.


---

Acceptance Criteria:

[ ] Define an ErrorLog entity class with fields for error type, error code, additional details, and timestamp.

[ ] Create an enum for error types with values like PAYMENT_FAILURE, LOGICAL_FAILURE, etc.

[ ] Implement a custom DAO layer:

Define a saveErrorLog() method in the DAO interface.

Provide an implementation using EntityManager for persisting error logs.


[ ] Develop a utility method (logError) to simplify logging errors by accepting parameters like error type, code, and details.

[ ] Ensure proper validation of inputs and non-nullable constraints.

[ ] Write unit tests for the DAO and utility methods.



---

Database Table Definition:


---

Priority:

High


---

Effort Estimate:

3 Story Points


---

Let me know if you need further refinement!


  Description:

Develop a reusable method in the DAO layer to log application errors with the following attributes:

1. Id Type :UUID.

2. Order Reference Number : A unique String to representing the specific order.

3. SBI Order Reference Number : A unique String to representing the specific order from SBI end.

4. ATRN Number : A unique String to representing the specific transaction success or failure.

5. Error Code: A unique Enum representing the specific error.

6. Pay Mode : A unique Enum representing the specific pay mode (eg. NB, CC, DC).

7. Failure Reason : A unique representing the specific failure reasion.

7. Entity : A Enum which represent the entity.

8. Error Message : Any String.
