# How to contribute

If you want to contribute, have a look at our issues list first to see what we think is needed. If you want to add something else, you're free to do so aswell.

## Testing & prototyping

You need to be able to run the system and run the JUnit tests in the `tests` package. Make sure you test from our clean source first, so that you know if something's broken because of your changes, or if the tests fail anyway.

If you want to test against the real implementation, there's a [Postman file](https://github.com/teiler/api.teiler.io/blob/master/src/main/resources/postman/Postman.json). Your contribution could also extend that Postman file (just export it after you're done). To run it, simply add a Postman environment variable `url` which should point to the server, e.g. `http://localhost:4567`.

Be aware that your contribution *should* also include new unit tests for what you've added or fixed, although we won't reject it just because of that.

## Dealing with API changes

If your change would add something to the API, or even change the behaviour, you need to add a proposal to our [API specification repository](https://github.com/teiler/doc.teiler.io) first. The contribution process for the API is written out there.

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
