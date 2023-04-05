#!/bin/bash

VERSION=${1:-latest}
docker build -f docker/Dockerfile . -t "maciek/ctr:$VERSION"