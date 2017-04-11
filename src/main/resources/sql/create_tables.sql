CREATE TYPE CURRENCY AS ENUM (
  'chf',
  'eur'
);

CREATE TYPE TRANSACTIONTYPE AS ENUM (
  'expense',
  'compensation'
);

CREATE TABLE "group" (
  "id"  VARCHAR(50),
  "name"  VARCHAR(50) NOT NULL,
  "currency"  VARCHAR(50) NOT NULL,
  "create_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  "update_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  PRIMARY KEY ("id")
);

CREATE TABLE "person" (
  "id"  SERIAL,
  "name"  VARCHAR(50) NOT NULL,
  "group" VARCHAR(50) REFERENCES "group" ON DELETE CASCADE,
  "create_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  "update_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  PRIMARY KEY ("id")
);

CREATE TABLE "transaction" (
  "id"  SERIAL,
  "title" VARCHAR(50) NOT NULL,
  "amount" INTEGER NOT NULL,
  "type" TRANSACTIONTYPE NOT NULL,
  "payer" INTEGER NOT NULL REFERENCES "person" ON DELETE CASCADE,
  "create_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  "update_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  PRIMARY KEY ("id")
);

CREATE TABLE "profiteer" (
  "person" INTEGER REFERENCES "person" ON DELETE CASCADE,
  "transaction" INTEGER REFERENCES "transaction",
  "factor" DECIMAL(5) NOT NULL,
  "create_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  "update_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  PRIMARY KEY ("person", "transaction")
);
