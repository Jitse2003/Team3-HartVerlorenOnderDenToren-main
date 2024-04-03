FROM maven:3.9.0-amazoncorretto-19 as build
COPY . /app
WORKDIR /app
RUN mvn clean install -DskipTests
FROM amazoncorretto:19
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]