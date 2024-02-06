# Fee Service

Fee Service is a Java application developed using Spring Boot (version 3.2.2) and Java 21. It serves as a backend service for configuring fees based on grades. The application utilizes an H2 database for data storage, Swagger for API documentation, and Actuator for monitoring.

## Features

1. **Configure Fee:**
    - POST `/fee` - Configure fees based on schoolId and grades.

2. **Update Fee:**
    - PUT `/fee` - Update fee.

3. **Get Fee by Grade:**
    - GET `/fee/grade?grade={grade}&schoolId={schoolId}` - Retrieve fee details for a specific grade and schoolId.
    - 
4. **Get All Configure Fee:**
    - GET `/fee` - Retrieve all configure fee


5. **Swagger Documentation:**
    - API documentation is available through Swagger for easy exploration and integration.
    - Accessible at http://localhost:8082/fee-service/swagger-ui/index.html#/

### Spring Boot Actuator

- Monitor and manage the application in production with Spring Boot Actuator.
- Endpoints include health, metrics, info, and more.
- Accessible at http://localhost:8082/fee-service/actuator

## Usage

1. Clone the repository:

   ```bash
   https://github.com/MuhammadFaizan17/fee-service.git

2. Build Application:

   ```bash
   mvn clean install

3. Run Application:

   ```bash
   java -jar fee-0.0.1-SNAPSHOT.jar

4. Access Swagger:

   ```bash
   http://localhost:8082/fee-service/swagger-ui/index.html#/
