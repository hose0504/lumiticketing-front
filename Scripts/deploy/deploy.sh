#!/bin/bash

set -e  # 에러 발생 시 스크립트 중단

echo "=== Lumiticketing 배포 시작 ==="

# 1. 변수 설정
ZIP_FILE="/home/ubuntu/regular_front_build.zip"
DEST_DIR="/home/ubuntu"
TOMCAT_DIR="/opt/tomcat/tomcat-10/webapps"
WAR_NAME="boot.war"

# 2. 기존 파일 제거
echo "[1/6] 기존 war 및 디렉토리 제거 중..."
sudo rm -f "$DEST_DIR/$WAR_NAME"
sudo rm -rf "$TOMCAT_DIR/boot"
sudo rm -f "$TOMCAT_DIR/$WAR_NAME"

# 3. 압축 해제
echo "[2/6] 압축 해제 중..."
if unzip -o "$ZIP_FILE" -d "$DEST_DIR"; then
    echo "압축 해제 완료"
else
    echo "압축 해제 실패! 파일 경로 확인 필요: $ZIP_FILE"
    exit 1
fi

# 4. war 파일 존재 여부 확인
if [ -f "$DEST_DIR/$WAR_NAME" ]; then
    echo "[3/6] $WAR_NAME 확인 완료"
else
    echo "[ERROR] $DEST_DIR/$WAR_NAME 파일이 존재하지 않습니다."
    exit 1
fi

# 5. 톰캣 webapps로 이동
echo "[4/6] $WAR_NAME → Tomcat webapps로 이동 중..."
sudo mv "$DEST_DIR/$WAR_NAME" "$TOMCAT_DIR/"

# 6. 톰캣 재시작
echo "[5/6] Tomcat 재시작 중..."
sudo systemctl restart tomcat

# 7. 완료
echo "[6/6] 배포 성공! 🎉"
exit 0
