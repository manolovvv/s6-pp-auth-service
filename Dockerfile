FROM openjdk:11

EXPOSE 8081

ADD target/auth-service-0.0.1-SNAPSHOT.jar auth-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT [ "java", "-jar" , "/auth-service-0.0.1-SNAPSHOT.jar" ]