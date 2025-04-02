#!/bin/bash

# S3에서 받은 zip 파일 경로
ZIP_FILE="/home/ubuntu/regular_front_build.zip"

# 압축을 풀 디렉터리
DEST_DIR="/home/ubuntu"

sudo rm /home/ubuntu/boot.war
sudo rm -r /opt/tomcat/tomcat-10/webapps/boot
sudo rm /opt/tomcat/tomcat-10/webapps/boot.war
# 압축 풀기
echo "압축을 풀고 있습니다..."
unzip -o $ZIP_FILE -d $DEST_DIR

# boot.war가 존재하는지 확인
if [ -f $DEST_DIR/boot.war ]; then
    # 톰캣의 webapps 디렉토리로 boot.war 옮기기
    echo "boot.war 파일을 톰캣 webapps로 옮기는 중..."
    sudo mv $DEST_DIR/boot.war /opt/tomcat/tomcat-10/webapps/
else
    echo "boot.war 파일이 존재하지 않습니다!"
    exit 1
fi

# 톰캣 재시작
echo "톰캣을 재시작 중..."
sudo systemctl restart tomcat

# 성공적으로 종료
echo "배포 성공!"
exit 0
