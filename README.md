# Teiler
Teiler is a REST API to help you to split up expenses in groups.

## Architecture

This project is the backend implementation for teiler. It implements the API specification created at [doc.teiler.io](https://github.com/teiler/doc.teiler.io).

## Clients

Everyone is invited to create their own clients. Here's a list of clients we know of:

* [web.teiler.io](https://github.com/teiler/web.teiler.io) (reference implementation)

## Software Architecture

Let's start by defining the domain of Teiler. Along, we'll define some terms so we're talking of the same thing

* There are groups with people in it
* People have names
* There are *transactions*, which is a umbrella term for two kinds of things:
  * When a person buys something for other people, she creates an *expense*. An expense is paid by one person and multiple people profit from it (*profiteers*). Also, expenses have titles.
  * When people pay money back, they create a *compensation*. Compensations are paid by one person and have only one profiteer
* If one person pays something and is not paid back, we have *debts*. From those debts, you can *settle up* the group which suggests which compensations need to be made to get all people in the group even.

In the actual code, it's packaged like that (from top to bottom if you think of layers)

* `endpoint`: The REST endpoints reside in this package. It's responsible for presentation and request handling. Request data is de-serialized from JSON to our internal representation (next layer), and any answers are serialized to JSON. Also, any limits and other parameters are set and read here. If an exception is thrown, the logic to convert them into HTTP error codes and JSON error literals is also here. 

Per endpoint, there's one class. Each class implements a single lambda method for each request.

* `dto`: This is the data as we need it for the business logic part.
* `services`: This is the actual flesh on the bones. There's one class per endpoint and one method per request. First, all the data given from the `endpoint` class is validated. If the data is valid, it's processed according to business logic rules and then sent to the persistence layer. 

Validation (along with other things) happens in the `services.util` package, so we can focus on actual business logic for the happy case in the `services` package. All errors are thrown via *exceptions*.

* `entities`: This is the data as we need it for the persistence part. Also, any conversion logic from `dto` to `entities` *and vice-versa* is stored here.
* `repositories`: This is the persistence layer where the logic resides on how to actually store and query data in the database. 
* `util`: At the very bottom, there are various tools, e.g. a helper for time serialization in GSON and classes for the exceptions and some enums.

## Run from binaries

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

5. Run `scripts/tylr-api` to start the TYLR.
6. Access it through http://localhost:PORT

If you want to access the API from outside localhost or / and SSL, use a reverse proxy in front of the application like an NGINX.

## Run from source

Prerequisites:
 
 - Java / OpenJDK 8
 - PostgreSQL 9
 
1. Install the prerequisites.
2. Clone the master branch or a version tag from Github.
3. Do steps 4 and 5 from above.
4. Run `./gradlew run` to run TYLR.
