FROM amazoncorretto:21-alpine
WORKDIR /app
COPY build/libs/ticketoffice-backend.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]