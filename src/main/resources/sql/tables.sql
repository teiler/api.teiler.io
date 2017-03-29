CREATE TYPE Currency AS ENUM (
  'CHF',
  'EUR'
);

CREATE TYPE TransactionType AS ENUM (
  'EXPENSE',
  'COMPENSATION'
);

CREATE TABLE IF NOT EXISTS "group" (
  uuid  VARCHAR(50),
  name  VARCHAR(50) NOT NULL,
  currency  Currency NOT NULL,
  PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS "person" (
  id  INTEGER,
  name  VARCHAR(50) NOT NULL,
  groupUuid VARCHAR(50) NOT NULL REFERENCES "group",
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "transaction" (
  id  INTEGER,
  title VARCHAR(50) NOT NULL,
  amount INTEGER NOT NULL,
  createTime TIMESTAMP WITH TIME ZONE NOT NULL,
  updateTime TIMESTAMP WITH TIME ZONE NOT NULL,
  transactionType TransactionType NOT NULL,
  payerId INTEGER NOT NULL REFERENCES "person",
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS "profiteer" (
  personId INTEGER REFERENCES "person",
  transactionId INTEGER REFERENCES "transaction",
  factor DECIMAL(5) NOT NULL,
  PRIMARY KEY (personId, transactionId)
);
