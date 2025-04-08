#!/bin/bash

echo "-------------Start Server-------------"
cd /home/ubuntu/fitday
if [ ! -d "./certbot-etc" ]; then
  echo "certbot-etc 폴더 생성"
  sudo mkdir certbot-etc
else
  echo "certbot-etc 폴더가 이미 존재합니다."
fi
if [ ! -d "./web" ]; then
  echo "web 폴더 생성"
  sudo mkdir web
else
  echo "web 폴더가 이미 존재합니다."
fi
docker stop github-actions || true
docker stop fitDay-mysql || true
docker stop fitDay-redis || true
docker rm github-actions || true
docker rm fitDay-mysql || true
docker rm fitDay-redis || true
docker-compose up -d
docker pull 390402538983.dkr.ecr.ap-northeast-2.amazonaws.com/github-actions:latest
docker run -d --name github-actions -p 8080:8080 390402538983.dkr.ecr.ap-northeast-2.amazonaws.com/github-actions:latest
echo "-------------End-------------"