# FROM openjdk:17
# ARG JAR_FILE=target/*.jar
# COPY ${JAR_FILE} app.jar 
# ENTRYPOINT ["java","-jar","/app.jar"]
FROM maven:3.2.0-open-jdk-17

WORKDIR /exams-ras
COPY .
RUN mvn clean install

CMD mvn spring-boot:run