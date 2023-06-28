FROM openjdk:17-jdk
WORKDIR /opt
ADD target/staff-manager-api-*.jar staff-manager-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/staff-manager-api.jar"]