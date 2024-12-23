# Question 5 - Part 3 : Enhancement with Distributed Transaction Management, Data Integrity and Failure Handling for Idempotency
## Out of the 10 Issues that I mentioned in my asnwer for part 2, I have implemented 3 of the solutions in this project.
## The most basic implementation is a Login Authentication and Authorization; but I have deliberately skipped it
    ~ A login system is equivalent to "Hello World" of Spring Web Applications

## Mitigations -
1. Distributed Transaction Management 
    i. Ensures that the debit and credit operations are consistent and are either fully completed or rolled back in case of failure.
2. Idempotency ensures that the same transaction ID is processed only once, even if the request is sent multiple times.
    i. The ReceiverController stores transaction IDs in a set (processedTransactionIds) to ensure that each transaction ID is processed only once.
    ii. If the Receiver service gets a transaction with a previously processed transaction ID, it returns a 200 OK response indicating the request is a duplicate.
3. Data Integrity ensures that the data (amount and account) is not tampered with during the transaction.
    i. A hash (Integrity-Hash) is generated based on the data (account ID and amount) to ensure that the data was not tampered with during transmission.
    ii. The Receiver service recalculates the hash on its side and compares it with the incoming hash to validate the integrity of the request.
    iii. If the Receiver service fails, the Payee service will roll back the debit operation, ensuring the system remains     consistent.
4. The services are run over HTTP, making it simpler for local testing and debugging.

## Project Overview - This project consists of two Spring Boot microservices:
1. **Payee System**: Responsible for handling fund transfers by debiting the sender's account and interacting with the Receiver System to credit the recipient's account.
2. **Receiver System**: Handles crediting the recipient's account while ensuring data integrity and idempotency.

## Features
### Payee System
- Generates a unique transaction ID for each transfer.
- Debits the sender's account.
- Communicates with the Receiver System to credit the recipient's account.
- Implements data integrity by validating hash values.
- Supports transaction rollback in case of failures.

### Receiver System
- Credits the recipient's account.
- Ensures data integrity by validating hash values provided by the Payee System.
- Implements idempotency by rejecting duplicate transactions.

## Endpoints
### Payee System
#### **POST /payee/transfer**
- Handles fund transfers between accounts.

**Request Parameters**:
- `senderAccountId`: The ID of the sender's account.
- `receiverAccountId`: The ID of the receiver's account.
- `amount`: The amount to transfer.

**Response**:
- `200 OK`: Transfer was successful.
- `500 Internal Server Error`: Transfer failed due to server issues or validation errors.

**Example cURL Command**:
```bash
curl -X POST "http://localhost:8080/payee/transfer?senderAccountId=AccA1&receiverAccountId=AccA1&amount=100" -H "Content-Type: application/json"
```

### Receiver System
#### **POST /receiver/credit**
- Handles crediting the recipient's account.

**Request Parameters**:
- `accountId`: The ID of the receiver's account.
- `amount`: The amount to credit.
- `senderId`: The ID of the sender's account.

**Headers**:
- `Integrity-Hash`: Hash for data integrity validation.
- `Transaction-Id`: Unique transaction ID for idempotency.

**Response**:
- `200 OK`: Amount credited successfully or duplicate transaction detected.
- `400 Bad Request`: Integrity check failed.

**Example cURL Command**:
```bash
curl -X POST "http://localhost:8081/receiver/credit" \
     -H "Content-Type: application/json" \
     -H "Integrity-Hash: <calculated-hash>" \
     -H "Transaction-Id: <unique-id>" \
     -d '{"accountId":"AccB1", "amount":100, "senderId":"AccA1"}'
```
## Distributed Transaction Management
To handle distributed transactions across the Payee and Receiver Systems, the following process is followed:

Prepare Phase:
- The Payee System debits the sender's account and records the intent to transfer.
- The Receiver System validates that it can credit the amount.
- Both systems share the transaction ID and prepare to commit.

Commit Phase:
- If both systems are prepared, they commit the transaction.
- In case of any failure during the prepare phase, the systems roll back any partial changes.

Rollback:
- If a failure occurs at any point, the Payee System rolls back the debit, and the Receiver System discards the credit intent.
- This ensures atomicity and consistency in distributed environments.

## Data Integrity
Both systems use SHA-256 hashing for data integrity validation. The hash is computed as follows:

```java
String payload = senderAccountId + receiverAccountId + amount;
String integrityHash = generateHash(payload);
```

The Receiver System recalculates the hash and compares it with the provided value to ensure the request has not been tampered with.

## Idempotency
The Receiver System ensures idempotency by maintaining a set of processed transaction IDs. Duplicate requests with the same transaction ID are ignored.

## Project Setup
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-folder>
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the Project:
   ```bash
   cd payee-system
   mvn spring-boot:run
   ```

## Testing
Unit tests are provided for both systems. To run tests:

```bash
mvn test
```

## Future Enhancements
- Add authentication and authorization.
- Implement database storage for accounts and transaction data.
- Enhance error handling and logging.