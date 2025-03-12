FROM openjdk:21-jdk-slinim

WORKDIR /app

COPY target/microservice_image-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT [ "java","-jar","/app/app.jar" ]