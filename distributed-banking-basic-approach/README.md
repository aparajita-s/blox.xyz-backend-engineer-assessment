# Question 5 - Basic approach to handle distributed payment system.
## Project Overview - Implemented two systems as the payee's code runs on different server than receiver
## Features
### Payee System
- Debits the sender's account.

### Receiver System
- Credits the recipient's account.

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
curl -X POST "http://localhost:8080/payee/transfer?senderAccountId=accA&receiverAccountId=accB&amount=1000"
```

### Receiver System
#### **POST /receiver/credit**
- Handles crediting the recipient's account.

**Request Parameters**:
- `accountId`: The ID of the receiver's account.
- `amount`: The amount to credit.

**Response**:
- `200 OK`: Transfer Successful.

**Example cURL Command**:
```bash
curl -X POST "http://localhost:8081/receiver/credit"  -d '{"accountId":"AccB1", "amount":100}'
```

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
Used curl command to test.

## Future Enhancements
# Question 5 - Part 1 and Part 2 : Common Issues and its Mitigation
1. Network Reliability
Issue: The communication between the Payee and Receiver services depends on the network. If the network is down or experiences high latency, the transaction could fail or take longer than expected.
Mitigation:
Implement retry logic to handle transient failures.
Use message queues (e.g., RabbitMQ or Kafka) to ensure message delivery in case of temporary service unavailability.
2. Distributed Transaction Management
Issue: Transactions across two independent services need to maintain consistency. For example, if the money is debited from the sender's account but the receiver's service fails to credit the amount, the system ends up in an inconsistent state.
Mitigation:
Use the Saga Pattern:
Divide the transaction into a series of local transactions.
Implement compensating actions to roll back changes if a step fails.
Adopt event-driven architectures to ensure eventual consistency.
3. Security
Issue: Exchanging sensitive information (e.g., account details and transaction amounts) over the network is prone to interception or tampering.
Mitigation:
Use HTTPS to encrypt data in transit.
Implement authentication and authorization mechanisms, such as OAuth2, JWT, or API keys.
Validate input data on both sides to prevent malicious attacks.
4. Data Integrity
Issue: If data is corrupted or lost during transmission, the transaction may fail or lead to incorrect balances.
Mitigation:
Use checksums or hashes to verify the integrity of the data being transmitted.
Log every transaction at both the Payee and Receiver sides for traceability.
5. Latency
Issue: Real-time transactions may experience delays due to network latency, processing time, or congestion.
Mitigation:
Optimize API endpoints and database queries.
Use asynchronous communication where possible to improve performance.
6. Failure Handling
Issue: Failures at any point (e.g., service crashes, network outages) can lead to incomplete transactions.
Mitigation:
Use distributed logging and monitoring to quickly detect and recover from failures.
Implement idempotency in the Receiver Service to prevent duplicate credits if the same request is processed multiple times.
7. Scalability
Issue: As the number of transactions increases, both services need to handle the growing load without degrading performance.
Mitigation:
Use load balancers to distribute requests across multiple instances of each service.
Use caching for frequently accessed data.
8. Lack of Atomicity
Issue: The two operations (debit and credit) are not atomic since they occur in separate systems. A failure in either operation can leave the system in an inconsistent state.
Mitigation:
Implement a two-phase commit (2PC) mechanism (if using a distributed database).
Use event sourcing to ensure a record of all state changes.
9. Dependency on External Systems
Issue: The system relies on the availability and correctness of external services, such as third-party banks.
Mitigation:
Use fallback mechanisms to handle unavailable or incorrect responses from external systems.
Periodically reconcile balances between systems.
10. Regulatory and Compliance Challenges
Issue: Transferring money across banks often involves compliance with legal and regulatory standards (e.g., anti-money laundering, transaction limits).
Mitigation:
Integrate compliance checks as part of the transaction process.
Maintain audit trails for all transactions