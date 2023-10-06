FROM openjdk:8-jdk-alpine
RUN mkdir /app
WORKDIR /app
COPY app.jar /app
EXPOSE 3003
CMD ["java", "-jar", "app.jar"]