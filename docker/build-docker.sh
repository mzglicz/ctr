#!/bin/bash

VERSION=$1
docker build -f docker/Dockerfile . -t "maciek/ctr:$VERSION"