#!/usr/bin/env bash

sudo $(pwd)/mvnw clean

sudo $(pwd)/mvnw install

sudo $(pwd)/mvnw docker:build

if [ $? -ne 0 ]; then
  echo ''
else
  exit 'false'
fi

sudo docker-compose -f docker-compose.yml -f docker-compose-mongo.yml -f docker-compose-redis.yml up
