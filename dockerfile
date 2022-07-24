# CI가 성공했을 시 작동하는 스크립트
# 이미 빌드된 파일이 존재한다.
# build된 jar는 libs에 존재
FROM openjdk:11

ENV buildDir build/libs/
ENV jarName *SNAPSHOT.jar

RUN mkdir /app
COPY $buildDir$jarName /app
WORKDIR /app
CMD java -jar $jarName
