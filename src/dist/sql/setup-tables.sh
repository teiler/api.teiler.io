#!/bin/sh
set -e
set -x

cd "$(dirname "$0")"

psql -U tylr -f 10_runEverytime.sql
