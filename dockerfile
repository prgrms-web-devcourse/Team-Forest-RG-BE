
FROM openjdk:11

# CI가 성공해서 build된 jar가 이미 존재합니다.
ENV JAR_PATH=build/libs
ENV JAR_NAME=*SNAPSHOT.jar

RUN mkdir /app
COPY $JAR_PATH/$JAR_NAME /app
WORKDIR /app

# Profile을 배포용으로 적용할 경우(prod)
# java -jar -Dspring.profiles.active=prod $jarName
CMD java -jar $JAR_NAME
