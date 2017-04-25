#!/bin/sh

set -e
set -x

eval "$(ssh-agent -s)"
chmod 600 .travis/deploy_key.pem
ssh-add .travis/deploy_key.pem

scp build/distributions/tylr-api.tar tylr@api.teiler.io:/home/tylr/
ssh tylr@api.teiler.io << EOF
 sudo /etc/init.d/tylr-api stop
 tar -xvf /home/tylr/tylr-api.tar -C /srv/http/tylr-api/  --overwrite --strip-components=1
 sudo /etc/init.d/tylr-api start
EOF

./.travis/test-prod.sh
