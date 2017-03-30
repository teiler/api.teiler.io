/*
Use the following command (no -U)
psql postgres -f <file>
 */

SET client_min_messages = ERROR;
\set user tylr
\set password '\'tylr\''
\set database tylr
\set promptvar ''

DROP DATABASE if exists :database;
DROP USER if exists :user;
