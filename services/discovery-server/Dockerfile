FROM maven:latest AS build

WORKDIR /app

COPY pom.xml /bom/pom.xml
RUN mvn -f /bom/pom.xml install -N

COPY services/discovery-server/.mvn .mvn
COPY services/discovery-server/mvnw services/discovery-server/pom.xml ./

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} service.jar
ENTRYPOINT ["java", "-jar", "service.jar"]
