#`!/bin/sh

set -e
set -x

[ -d node_modules ] && rm -rf node_modules
npm install
npm install -g mocha
mocha src/test/chakram
