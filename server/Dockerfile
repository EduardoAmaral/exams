FROM gradle:6.5.1-jdk11 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar

FROM openjdk:11-jre-slim

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/server-*.jar /app/server.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/server.jar"]