#!/bin/bash
set -e

echo "=== Lumiticketing 배포 시작 ==="

# 1. 변수 설정 (현재 디렉토리 기준)
ZIP_FILE="./regular_front_build.zip"
DEST_DIR="."
TOMCAT_DIR="/opt/tomcat/tomcat-10/webapps"
WAR_NAME="boot.war"

# 2. 기존 파일 제거
echo "[1/6] 기존 WAR 및 디렉토리 제거 중..."
sudo rm -f "$TOMCAT_DIR/$WAR_NAME"
sudo rm -rf "$TOMCAT_DIR/boot"

# 3. 압축 해제
echo "[2/6] 압축 해제 중..."
unzip -o "$ZIP_FILE" -d "$DEST_DIR"

# 4. WAR 존재 확인
if [ -f "$DEST_DIR/$WAR_NAME" ]; then
    echo "[3/6] $WAR_NAME 확인 완료"
else
    echo "[ERROR] $DEST_DIR/$WAR_NAME 파일이 존재하지 않습니다."
    exit 1
fi

# 5. WAR 이동
echo "[4/6] $WAR_NAME → Tomcat webapps로 이동 중..."
sudo mv "$DEST_DIR/$WAR_NAME" "$TOMCAT_DIR/"
sudo chown tomcat:tomcat "$TOMCAT_DIR/$WAR_NAME"

# 6. Tomcat 재시작
echo "[5/6] Tomcat 재시작 중..."
sudo systemctl restart tomcat

# 7. 완료
echo "[6/6] 배포 성공! 🎉"
exit 0
