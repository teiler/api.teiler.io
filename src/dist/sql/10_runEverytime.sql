/*
Use the following command (no -U)
psql postgres -f <file>
 */

SET client_min_messages = ERROR;
\set user tylr
\set database tylr
\set promptvar ''

\c :database :user

-- specify encoding to match your files encoding, usually UTF8
-- valid values are: 'UTF8', 'LATIN1', 'WIN1252'
\encoding 'UTF8'

\ir drop_tables.sql
-- create tables
\ir create_tables.sql

-- constraints and indexes

\encoding 'auto'

