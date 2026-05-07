FROM maven:latest AS build

WORKDIR /app

COPY pom.xml /bom/pom.xml
RUN mvn -f /bom/pom.xml install -N

COPY services/post-service/.mvn .mvn
COPY services/post-service/mvnw services/post-service/pom.xml ./

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} service.jar
ENTRYPOINT ["java", "-jar", "service.jar"]
