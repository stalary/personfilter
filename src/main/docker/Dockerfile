FROM openjdk:8-jdk-alpine
ADD personfilter-0.1.jar app.jar
# 注意时区问题
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+08","/app.jar"]
EXPOSE 6200