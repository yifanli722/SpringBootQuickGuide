# Stage 1: Build the application
FROM adoptopenjdk:11-jdk-hotspot AS build
WORKDIR /app
COPY . .
COPY src/main/resources/application.docker.yml src/main/resources/application.yml
RUN ./gradlew build

# Stage 2: Create the final Docker image
FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
COPY --from=build /app/build/libs/*.jar ./
EXPOSE 3000
#ENTRYPOINT ["tail", "-f", "/dev/null"]
CMD ["java", "-jar", "SpringBootQuickGuide-1.0.0.jar"]
