# TYLR
TYLR is a REST API to help you to split up expenses in groups.

TODO:
* Port
* SQL create user

## Run from binaries

Perquisites:
 
 - Java / OpenJDK 8
 - PostgreSQL 9
 
1. Install the perquisites.
2. Download the newest release form Github.
3. Run the following commands to create a user and database:

````
CREATE USER tylr IDENTIFIED BY 'tylr';
CREATE DATABASE tylr WITH OWNER tylr ENCODING 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8'
GRANT ALL PRIVILEGES ON DATABASE tylr- to tylr;
````

````
psql -f sql/create_tables.sql tylr tylr
````

4. Create the`applications.properties` file and add the database credentials.

````
# Datasource settings
spring.datasource.url = jdbc:postgresql://localhost:5432/tylr
spring.datasource.username = tylr
spring.datasource.password = tylr
#server.port = 4567 #default
#server.ip = 127.0.0.1 #default
````

5. Run `scripts/tylr-api` to start the TYLR.
6. Access it through http://localhost:PORT

If you want to access the API from outside localhost or / and SSL, you need to use a reverse proxy in front of the application like an NGINX.

## Run from source

Perquisites:
 
 - Java / OpenJDK 8
 - PostgreSQL 9
 
1. Install the perquisites.
2. Clone the master branch or a version tag from Github.
3. Do steps 4 and 5 from above.
4. Run `./gradlew run` to run TYLR.