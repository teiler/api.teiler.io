/*
Use the following command (no -U)
psql postgres -f <file>
 */

SET client_min_messages = ERROR;
\set user tylr
\set database tylr
\set promptvar ''

-- terminate sessions
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = ':database'
  AND pid <> pg_backend_pid();

CREATE DATABASE :database WITH OWNER :user ENCODING 'UTF8'
LC_COLLATE = 'en_US.UTF-8'
LC_CTYPE = 'en_US.UTF-8'
TEMPLATE template0;

GRANT ALL PRIVILEGES ON DATABASE :database to :user;
