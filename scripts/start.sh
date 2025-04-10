#!/bin/bash

echo "-------------Start Server-------------"
cd /home/ubuntu/fitday
if [ ! -d "./certbot-etc" ]; then
  echo "certbot-etc 폴더 생성"
  sudo mkdir certbot-etc
else
  echo "certbot-etc 폴더가 이미 존재합니다."
fi
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

docker-compose -f docker-compose.yml up -d

#if ! docker ps | grep -q nginxproxy; then
#  echo "nginxproxy 컨테이너가 실행 중이 아닙니다. docker-compose-nginx.yml 실행합니다."
#  docker-compose -f docker-compose-nginx.yml up -d
#else
#  echo "nginxproxy 컨테이너가 이미 실행 중입니다."
#fi

docker pull 390402538983.dkr.ecr.ap-northeast-2.amazonaws.com/github-actions:latest
docker run -d --name github-actions --network fitday-network -p 8080:8080 390402538983.dkr.ecr.ap-northeast-2.amazonaws.com/github-actions:latest
echo "-------------End-------------"