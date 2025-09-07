## Setup Instructions for Running the Project Locally

### 1. Clone the Repository

git clone https://github.com/venkatagopikarthik/LiveStockifyTask


2. Prerequisites
Java 17+ 

Maven (for building and running)



3. Build the Project
mvn clean install

4. Run the Application
mvn spring-boot:run

By default, the backend runs at:
http://localhost:8081

7. Test the Endpoints
Get all quotes in postman:
GET http://localhost:8081/api/quotes
