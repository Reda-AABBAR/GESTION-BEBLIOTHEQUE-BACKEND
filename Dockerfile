# Use a multi-stage build for a smaller final image
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only the necessary files for dependency resolution
COPY pom.xml ./
# Caching dependencies to speed up builds
RUN mvn dependency:go-offline -B

# Copy the entire project
COPY . ./
# Build the application, skipping tests for faster builds
RUN mvn clean package -DskipTests

# Use a smaller base image for the final image
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the built jar file to the final image
COPY --from=build /app/target/*.jar app.jar

# Expose the port on which the application will run
EXPOSE 8080

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "app.jar"]