# sm-platform
Setup Instructions
1. Configure Database

   1. Open the application.properties file.
   2. Set the following properties with your local database details:

   spring.datasource.url=jdbc:mysql://localhost:3306/<your_db_name>
   spring.datasource.username=<your_username>
   spring.datasource.password=<your_password>

   3. Replace <your_db_name>, <your_username>, and <your_password> with the correct values.
   

2. Set Up Redis.

   Ensure Redis is installed and running on your system.

3. Register a New User.

   Use the following endpoint to register a user:

   POST http://localhost:8080/api/v1/users/register

4. Login to the System.

   Use the following endpoint to log in:
   POST http://localhost:8080/api/v1/users/login 

5. Include the JWT token in the Authorization header for all endpoints except /register and /login:

   Authorization: Bearer <your_token>

   Replace <your_token> with the actual token from the login endpoint.

6. Using Swagger
   Access Swagger UI to explore and test APIs:

   http://localhost:8080/swagger-ui/index.html

7. Use Endpoints using Swagger
   1. Register a user via /register. 
   2. Log in via /login to get the JWT token. 
   3. Click the Authorize button in Swagger UI and enter the token in the format: 
      Bearer <your_token>
   4. Test all available endpoints.

Notes:

1. Ensure your database and Redis instances are running before starting the application.
2. All configuration details (e.g., ports, credentials) are in the application.properties file.
3. Keep your JWT token secure and ensure it has a reasonable expiration time.

