FROM openjdk:21
COPY target/user*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]