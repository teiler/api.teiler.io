#!/bin/sh

set -e
set -x

eval "$(ssh-agent -s)"
chmod 600 .travis/deploy_key.pem
ssh-add .travis/deploy_key.pem

scp build/distributions/tylr-api.tar tylr@api.teiler.io:/srv/http/tylr-api
