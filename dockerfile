
FROM openjdk:11

# CI가 성공해서 build된 jar가 이미 존재합니다.
ENV JAR_PATH=build/libs
ENV JAR_NAME=*SNAPSHOT.jar

RUN mkdir /app
COPY $JAR_PATH/$JAR_NAME /app
WORKDIR /app

# profile : prod
CMD java -jar -javaagent:pinpoint-bootstrap-2.4.0.jar -Dpinpoint.agentId=rg -Dpinpoint.applicationName=rg_server -Dpinpoint.config=pinpoint-root.config -Dspring.profiles.active=prod $JAR_NAME
