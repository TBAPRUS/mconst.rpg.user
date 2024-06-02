FROM openjdk:17
COPY target/user*.jar app.jar
ENTRYPOINT ["java", "-cp", "app.jar", "mconst.rpg.user.UserApplication"]
