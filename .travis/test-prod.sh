#!/bin/sh

set -e
set -x

[ -d node_modules ] && rm -rf node_modules
npm install
npm install -g mocha
env tylrurl="https://api.teiler.io/" mocha src/test/chakram
