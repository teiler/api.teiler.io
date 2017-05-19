--
-- MIT License
--
-- Copyright (c) 2017 L. Röllin, P. Bächli, K. Thurairatnam & D. Thoma
--
-- Permission is hereby granted, free of charge, to any person obtaining a copy
-- of this software and associated documentation files (the "Software"), to deal
-- in the Software without restriction, including without limitation the rights
-- to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
-- copies of the Software, and to permit persons to whom the Software is
-- furnished to do so, subject to the following conditions:
--
-- The above copyright notice and this permission notice shall be included in all
-- copies or substantial portions of the Software.
--
-- THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
-- IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
-- FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
-- AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
-- LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
-- OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
-- SOFTWARE.
--

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
  "active" BOOLEAN DEFAULT TRUE,
  "create_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  "update_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  PRIMARY KEY ("id")
);

CREATE UNIQUE INDEX unique_name_group ON person (name, "group") WHERE active = true;

CREATE TABLE "transaction" (
  "id"  SERIAL,
  "title" VARCHAR(50),
  "type" VARCHAR(50) NOT NULL,
  "payer" INTEGER REFERENCES "person" ON DELETE CASCADE,
  "create_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  "update_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  PRIMARY KEY ("id")
);

CREATE TABLE "profiteer" (
  "id"  SERIAL,
  "person" INTEGER REFERENCES "person" ON DELETE CASCADE,
  "transaction" INTEGER REFERENCES "transaction" ON DELETE CASCADE,
  "share" INTEGER,
  "create_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  "update_time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  PRIMARY KEY ("person", "transaction")
);
