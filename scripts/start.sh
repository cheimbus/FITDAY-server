#!/bin/bash

echo "-------------Start Server-------------"
cd /home/ubuntu/fitday

docker stop github-actions || true
docker stop spring-app || true
docker stop fitDay-redis || true
docker rm github-actions || true
docker rm spring-app || true
docker rm fitDay-redis || true

docker network create fitday-network

set -a
source .env
set +a

docker rmi -f $API_SERVER_IMAGE

docker-compose -f docker-compose-nginx.yml restart nginxproxy
docker-compose -f docker-compose.yml up -d

rm .env
echo "-------------End Server-------------"