#!/bin/bash

echo "-------------Start Server-------------"
MYSQL_RUNNING=$(docker ps -q -f name=^fitDay-mysql$)
REDIS_RUNNING=$(docker ps -q -f name=^fitDay-redis$)

if [[ -z "$MYSQL_RUNNING" || -z "$REDIS_RUNNING" ]]; then
    echo "MySQL 또는 Redis가 실행 중이 아닙니다. docker-compose를 실행합니다."
    docker-compose up -d
else
    echo "MySQL과 Redis가 이미 실행 중입니다. docker-compose를 실행하지 않습니다."
fi
docker stop github-actions || true
docker rm github-actions || true
docker pull 390402538983.dkr.ecr.ap-northeast-2.amazonaws.com/github-actions:latest
docker run -d --name github-actions -p 8080:8080 390402538983.dkr.ecr.ap-northeast-2.amazonaws.com/github-actions:latest
echo "-------------End-------------"