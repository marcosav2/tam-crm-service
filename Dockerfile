FROM maven:3.9.9-eclipse-temurin-23-alpine AS build
COPY . /source
WORKDIR /source
RUN mvn install

FROM eclipse-temurin:21-jdk-jammy
COPY --from=build /source/crm-service/boot/target/*.jar ./crm-service-boot.jar

CMD "java" "-jar" "crm-service-boot.jar"