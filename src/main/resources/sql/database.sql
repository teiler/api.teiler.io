CREATE USER tylr WITH PASSWORD 'tylr';

/* terminate sessions */
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'tylr'
  AND pid <> pg_backend_pid();

CREATE DATABASE tylr WITH OWNER 'tylr'
ENCODING 'UTF8'
LC_COLLATE = 'en_US.UTF-8'
LC_CTYPE = 'en_US.UTF-8'
TEMPLATE template0;

GRANT ALL PRIVILEGES ON DATABASE tylr to tylr;
