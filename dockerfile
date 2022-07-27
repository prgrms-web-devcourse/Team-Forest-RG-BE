
FROM openjdk:11

# CI가 성공해서 build된 jar가 이미 존재합니다.
ENV JAR_PATH=build/libs
ENV JAR_NAME=*SNAPSHOT.jar

# naver pinpoint 설치
RUN mkdir /app
WORKDIR /app
RUN wget https://github.com/pinpoint-apm/pinpoint/releases/download/v2.4.0/pinpoint-agent-2.4.0.tar.gz
RUN tar xvfz pinpoint-agent-2.4.0.tar.gz
COPY StringReplacer.java .
RUN javac StringReplacer.java && java StringReplacer
RUN rm -rf StringReplacer*

COPY $JAR_PATH/$JAR_NAME /app

# profile : prod
CMD java -jar -javaagent:/app/pinpoint-agent-2.4.0/pinpoint-bootstrap-2.4.0.jar -Dpinpoint.agentId=rg -Dpinpoint.applicationName=rg_server -Dpinpoint.config=pinpoint-root.config -Dspring.profiles.active=prod $JAR_NAME
