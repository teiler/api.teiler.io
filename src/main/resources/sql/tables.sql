CREATE TYPE currency AS ENUM (
  'chf',
  'eur'
);

CREATE TYPE transaction_type AS ENUM (
  'expense',
  'compensation'
);

CREATE TABLE IF NOT EXISTS "group" (
  "id"  VARCHAR(50),
  "name"  VARCHAR(50) NOT NULL,
  "currency"  currency NOT NULL,
  PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "person" (
  "id"  SERIAL,
  "name"  VARCHAR(50) NOT NULL,
  "group" VARCHAR(50) NOT NULL REFERENCES "group",
  PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "transaction" (
  "id"  SERIAL,
  "title" VARCHAR(50) NOT NULL,
  "amount" INTEGER NOT NULL,
  "createTime" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  "updateTime" TIMESTAMP WITH TIME ZONE NOT NULL,
  "type" transaction_type NOT NULL,
  "payer" INTEGER NOT NULL REFERENCES "person",
  PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "profiteer" (
  "person" INTEGER REFERENCES "person",
  "transaction" INTEGER REFERENCES "transaction",
  "factor" DECIMAL(5) NOT NULL,
  PRIMARY KEY ("person", "transaction")
);
