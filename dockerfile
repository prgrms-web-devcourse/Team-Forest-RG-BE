FROM openjdk:11

# 시스템으로 부터 build 실행 명령 --build-arg 매개변수로 가져올 변수들. (build-time arg)
ARG PINPOINT_HOST
ARG SLACK_CHANNEL_NAME
ARG SLACK_AUTH_TOKEN

# CI가 성공해서 build된 jar가 이미 존재합니다.
ENV JAR_PATH=build/libs
ENV JAR_NAME=*SNAPSHOT.jar
ENV PINPOINT_DIR=/app/pinpoint-agent-2.4.0
ENV LOG_PATH=./log
ENV SLACK_CHANNEL_NAME=$SLACK_CHANNEL_NAME
ENV SLACK_AUTH_TOKEN=$SLACK_AUTH_TOKEN

# naver pinpoint 설치 및 환경 설정
RUN mkdir /app
WORKDIR /app
RUN wget https://github.com/pinpoint-apm/pinpoint/releases/download/v2.4.0/pinpoint-agent-2.4.0.tar.gz
RUN tar xvfz pinpoint-agent-2.4.0.tar.gz && rm -rf pinpoint-agent-2.4.0.tar.gz
COPY config/PinpointConfigurer.java .
RUN javac PinpointConfigurer.java && java PinpointConfigurer $PINPOINT_HOST && rm -rf PinpointConfigurer*

COPY $JAR_PATH/$JAR_NAME /app
RUN mkdir $LOG_PATH

# profile : prod
CMD java -jar -javaagent:$PINPOINT_DIR/pinpoint-bootstrap-2.4.0.jar -Dpinpoint.agentId=rg -Dpinpoint.applicationName=rg_server -Dpinpoint.config=$PINPOINT_DIR/pinpoint-root.config -Dspring.profiles.active=prod,aws,security $JAR_NAME
