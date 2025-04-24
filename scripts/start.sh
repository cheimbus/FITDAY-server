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
docker pull $API_SERVER_IMAGE

docker run -d \
  --name github-actions \
  -p 8082:8080 \
  -v /home/ubuntu/pinpoint:/home/ubuntu/pinpoint:ro \
  -network fitday-network
  ${API_SERVER_IMAGE} \
    java \
      -javaagent:/home/ubuntu/pinpoint/pinpoint-bootstrap.jar \
      -Dpinpoint.config=/home/ubuntu/pinpoint/pinpoint-root.config \
      -Dpinpoint.agentId=fitday \
      -Dpinpoint.applicationName=fitday-app \
      -Dspring.profiles.active=dev \
      -jar /app.jar

docker-compose -f docker-compose-nginx.yml restart nginxproxy
docker-compose -f docker-compose.yml up -d redis

rm .env
echo "-------------End Server-------------"