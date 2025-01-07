Title: Fetch and Analyze Error Log Data by Merchant ID and Date Range

Description:

Develop a feature to fetch data from the error log table based on a provided mid (Merchant ID) and a specified date range (fromDate to toDate). The data should be grouped by failure reason, and only entries with an existing atrn in the transaction table should be considered. After fetching the data, calculate and segregate the failure percentage for each failure reason.

Acceptance Criteria:

1. Input Validation:

The system must validate that mid, fromDate, and toDate are provided.

Ensure fromDate is not later than toDate.



2. Data Fetching:

Verify if the atrn number exists in the transaction table for the provided mid.

Fetch error log entries from the error log table for the given mid and within the specified date range.



3. Data Grouping:

Group the fetched error log data by failure reason.



4. Failure Percentage Calculation:

Calculate the total number of errors for the mid and date range.

Determine the count and percentage of errors for each failure reason relative to the total errors.



5. Output:

Return a structured response containing each failure reason, the corresponding error count, and its failure percentage.



6. Error Handling:

Handle scenarios where no atrn exists in the transaction table.

Provide appropriate error messages for invalid inputs or empty results.



7. Performance:

Ensure that the solution is optimized for handling large datasets efficiently.




By fulfilling these criteria, the feature will provide insights into error trends and failure reasons for a specified merchant, aiding in diagnostic and decision-making processes.

