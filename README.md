# Teiler

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/da2f479780b7421d96a3f42faa55f313)](https://www.codacy.com/app/Teiler/api.teiler.io?utm_source=github.com&utm_medium=referral&utm_content=teiler/api.teiler.io&utm_campaign=badger)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/da2f479780b7421d96a3f42faa55f313)](https://www.codacy.com/app/Teiler/api.teiler.io?utm_source=github.com&utm_medium=referral&utm_content=teiler/api.teiler.io&utm_campaign=Badge_Coverage)

Teiler is a REST API to help you to split up expenses in groups.

## Run with docker

Prerequisites:

 - Docker

1. Clone the master branch or a version tag from Github.
2. Run `docker-compose up -d teiler` to run the server.

## Run from precompiled binaries

Prerequisites:

 - Java / OpenJDK 8
 - PostgreSQL 9

1. Install the prerequisites.
2. Download the newest release from Github.
3. Run the following commands to create a user and database:

````
CREATE USER tylr IDENTIFIED BY 'tylr';
CREATE DATABASE tylr WITH OWNER tylr ENCODING 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8'
GRANT ALL PRIVILEGES ON DATABASE tylr to tylr;
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

5. Run `scripts/tylr-api` to start the server.
6. Access it through http://localhost:PORT

If you want to access the API from outside localhost or / and SSL, use a reverse proxy in front of the application like an NGINX.

## Run from source

Prerequisites:

 - Java / OpenJDK 8
 - PostgreSQL 9

1. Install the prerequisites.
2. Clone the master branch or a version tag from Github.
3. Do steps 4 and 5 from above.
4. Run `./gradlew run` to run the server.

## Architecture

This project is the backend implementation for teiler. It implements the API specification created at [doc.teiler.io](https://github.com/teiler/doc.teiler.io).

## Clients

Everyone is invited to create their own clients. Here's a list of clients we know of:

* [web.teiler.io](https://github.com/teiler/web.teiler.io) (reference implementation)

## Contributing

For contribution guidelines and software architectural help, see [CONTRIBUTING](CONTRIBUTING.md)
