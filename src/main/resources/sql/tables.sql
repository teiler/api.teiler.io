CREATE TYPE CURRENCY AS ENUM (
  'chf',
  'eur'
);

CREATE TYPE TRANSACTIONTYPE AS ENUM (
  'expense',
  'compensation'
);

CREATE TABLE IF NOT EXISTS "group" (
  "id"  VARCHAR(50),
  "name"  VARCHAR(50) NOT NULL,
  "currency"  CURRENCY NOT NULL,
  "create_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  "update_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "person" (
  "id"  SERIAL,
  "name"  VARCHAR(50) NOT NULL,
  "group" VARCHAR(50) NOT NULL REFERENCES "group",
  "create_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  "update_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "transaction" (
  "id"  SERIAL,
  "title" VARCHAR(50) NOT NULL,
  "amount" INTEGER NOT NULL,
  "type" TRANSACTIONTYPE NOT NULL,
  "payer" INTEGER NOT NULL REFERENCES "person",
  "create_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  "update_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "profiteer" (
  "person" INTEGER REFERENCES "person",
  "transaction" INTEGER REFERENCES "transaction",
  "factor" DECIMAL(5) NOT NULL,
  "create_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  "update_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  PRIMARY KEY ("person", "transaction")
);
