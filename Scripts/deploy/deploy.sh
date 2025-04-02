#!/bin/bash

# boot.war가 존재하는지 확인
if [ -f /home/ubuntu/boot.war ]; then
    sudo cp /home/ubuntu/boot.war /opt/tomcat/tomcat-10/webapps/
else
    echo "boot.war 파일이 존재하지 않습니다!"
    exit 1
fi

# 톰캣 재시작
sudo systemctl restart tomcat

# 성공적으로 종료
exit 0
