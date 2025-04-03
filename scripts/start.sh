#!/bin/bash

echo "-------------Start Server-------------"
cd /home/ubuntu/fitday
# MySQL, Redis 실행 상태 확인
MYSQL_RUNNING=$(docker ps --filter "name=fitDay-mysql" --format "{{.Names}}")
REDIS_RUNNING=$(docker ps --filter "name=fitDay-redis" --format "{{.Names}}")

# 실행 중이 아닐 경우에만 docker-compose 실행
if [ -z "$MYSQL_RUNNING" ] || [ -z "$REDIS_RUNNING" ]; then
  echo "🚀 MySQL 또는 Redis가 실행되지 않음. docker-compose 실행!"
  docker-compose up -d
else
  echo "✅ MySQL과 Redis가 이미 실행 중. docker-compose 실행 안 함."
fi
docker stop github-actions || true
docker rm github-actions || true
docker pull 390402538983.dkr.ecr.ap-northeast-2.amazonaws.com/github-actions:latest
docker run -d --name github-actions -p 8080:8080 390402538983.dkr.ecr.ap-northeast-2.amazonaws.com/github-actions:latest
echo "-------------End-------------"