FROM gradle:6.5.1-jdk11 as builder

COPY --chown=gradle:gradle . /server
WORKDIR /server
RUN gradle clean build -x test --no-daemon

FROM openjdk:11-jre-slim

EXPOSE 8081

RUN mkdir /app

COPY --from=builder /server/build/libs/*.jar /app/server.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar" , "/app/server.jar"]