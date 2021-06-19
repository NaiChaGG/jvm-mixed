#!/usr/bin/env bash
docker-compose -f docker-compose-env-redis-phpRedisAdmin.yml down
docker-compose -f docker-compose-env-redis-phpRedisAdmin.yml up