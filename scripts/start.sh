#!/bin/bash

echo "-------------Start Server-------------"
cd /home/ubuntu/fitday
docker stop github-actions || true
docker stop fitDay-mysql || true
docker stop fitDay-redis || true
docker rm github-actions || true
docker rm fitDay-mysql || true
docker rm fitDay-redis || true
if ! docker network ls | grep -q fitday-network; then
  docker network create fitday-network
  echo "fitday-network 네트워크 생성 완료"
else
  echo "fitday-network 네트워크가 이미 존재합니다."
fi
set -a
source /home/runner/work/FITDAY-server/FITDAY-server/.env
set +a
echo "API_SERVER_IMAGE=$API_SERVER_IMAGE"
docker rmi -f $API_SERVER_IMAGE
docker pull $API_SERVER_IMAGE
docker-compose -f docker-compose-nginx.yml restart nginxproxy
docker-compose -f docker-compose.yml up -d redis github-actions
echo "-------------End Server-------------"