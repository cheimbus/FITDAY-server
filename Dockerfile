FROM eclipse-temurin:17-jdk-alpine
COPY ./build/libs/*SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
