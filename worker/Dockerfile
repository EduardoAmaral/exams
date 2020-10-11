FROM gradle:6.5.1-jdk11 as builder

COPY --chown=gradle:gradle . /worker
WORKDIR /worker
RUN gradle clean build -x test --no-daemon

FROM openjdk:11-jre-slim

EXPOSE 8083

RUN mkdir /app

COPY --from=builder /worker/build/libs/*.jar /app/worker.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar" , "/app/worker.jar"]