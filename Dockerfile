FROM openjdk:8-jdk-alpine

EXPOSE 8081

ADD target/auth-service-0.0.1-SNAPSHOT.jar auth-service-0.0.1-SNAPSHOT-SNAPSHOT.jar

ENTRYPOINT [ "java", "-jar" , "/auth-service-0.0.1-SNAPSHOT.jar" ]