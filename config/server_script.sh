RUN_CONTAINERS=$(sudo docker ps -a -q)

if [ "$RUN_CONTAINERS" ]
then
  sudo docker rm --force "$RUN_CONTAINERS"
fi

sudo docker pull epicblueha/rg:latest
sudo docker run epicblueha/rg:latest -p 80:80 -d \
--env RDBMS_URL="$RDBMS_URL" \
--env RDBMS_USERNAME="$RDBMS_USERNAME" \
--env RDBMS_PASSWORD="$RDBMS_PASSWORD"
