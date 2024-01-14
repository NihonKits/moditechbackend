FROM openjdk:11-jre-slim-buster
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]
EXPOSE 8080
# RUN mvn clean package -DskipTests
# ARG JAR_FILE=target/*.jar

