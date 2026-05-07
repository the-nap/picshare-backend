FROM maven:latest AS build

WORKDIR /app

COPY pom.xml /bom/pom.xml
RUN mvn -f /bom/pom.xml install -N

COPY services/user-service/.mvn .mvn
COPY services/user-service/mvnw services/user-service/pom.xml ./

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} service.jar
ENTRYPOINT ["java", "-jar", "service.jar"]
