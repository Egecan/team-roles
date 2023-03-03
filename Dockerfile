FROM gradle:7.4.0-jdk11 as build
WORKDIR /home/gradle/src
COPY build.gradle .
COPY settings.gradle .
COPY gradle.properties .
COPY src src
RUN gradle clean build

FROM openjdk:11-jre-slim
WORKDIR /
COPY --from=build /home/gradle/src/build/libs/teams-1.0-SNAPSHOT.jar /teams-1.0-SNAPSHOT.jar
CMD ["java", "-jar", "/teams-1.0-SNAPSHOT.jar"]