FROM maven:3.8.5-openjdk-17-slim as builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-alpine
WORKDIR /app
EXPOSE 8080:8080
COPY --from=builder /app/target/NavyManager-0.9.5.jar ./NavyManager.jar
CMD java -jar ./NavyManager.jar