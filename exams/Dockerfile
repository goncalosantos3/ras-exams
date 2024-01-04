FROM maven:3.8.3-openjdk-17

ENV SPRING_DATASOURCE_URL=jdbc:mysql://exams-mysql:5433/ras_exams
ENV DEFAULT_DB_URL=jdbc:mysql://exams-mysql:5433/

WORKDIR /exams-ras
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run
