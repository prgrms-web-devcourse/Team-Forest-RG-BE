# DB 관련 환경 변수는 CD 과정에서 적용됩니다.

image_name=epicblueha/rg:latest
running_container=$(sudo docker ps -q)
log_dir=$(pwd)/server_log
# 인스턴스에 이미 작동하는 컨테이너가 있으면 종료시킵니다.
if [ -n "$running_container" ]
then
  sudo docker rm -f "$running_container"
fi

sudo docker pull $image_name

if [ ! -d "$log_dir" ]
then
  mkdir "$log_dir"
fi

# Docker 관련 정보는 dockerfile 참조 바랍니다.
sudo docker run -p 8080:8080 -v "$log_dir":/app/log -d \
--env RDBMS_URL="$RDBMS_URL" \
--env RDBMS_USERNAME="$RDBMS_USERNAME" \
--env RDBMS_PASSWORD="$RDBMS_PASSWORD" \
$image_name
