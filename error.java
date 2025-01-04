Story Point for GitLab Ticket


---

Title:

Implement a Generic DAO for Logging Application Errors with Detailed Attributes


---

Description:

Develop a reusable method in the DAO layer to log detailed application error information into a centralized database table. The method will handle the following attributes:

1. Id Type: UUID to uniquely identify each error log entry.


2. Order Reference Number: A unique string representing the specific order.


3. SBI Order Reference Number: A unique string representing the order reference number from the SBI system.


4. ATRN Number: A unique string representing the specific transaction's success or failure.


5. Error Code: An enum categorizing errors (e.g., PAYMENT_FAILURE, LOGICAL_FAILURE, etc.).


6. Pay Mode: An enum representing the payment mode (e.g., NB, CC, DC).


7. Failure Reason: A string describing the specific reason for the failure.


8. Entity: An enum to represent the application entity associated with the error.


9. Error Message: A string for additional details or context about the error.



The error logs will be stored in a unified database table, allowing for easy debugging, monitoring, and analytics.


---

Acceptance Criteria:

[ ] Define an ErrorLog entity class with the required fields and appropriate constraints (nullable and non-nullable).

[ ] Create enum types for Error Code, Pay Mode, and Entity.

[ ] Implement a custom DAO layer:

Define a saveErrorLog() method in the DAO interface.

Provide an implementation using EntityManager for persisting error logs.


[ ] Develop a utility method to simplify logging errors by accepting parameters for all the attributes.

[ ] Ensure UUID is auto-generated and used as the primary key.

[ ] Write unit tests to validate DAO and utility method functionality.



---

Database Table Definition:


---

Priority:

High


---

Effort Estimate:

5 Story Points

Reason: The task requires defining multiple fields, creating enums, implementing a custom DAO, and ensuring proper error handling and validation.



---

Let me know if additional adjustments are needed!

