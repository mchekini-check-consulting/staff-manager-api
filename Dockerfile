FROM ubuntu:20.04
RUN apt-get update && apt-get install openjdk-17-jdk vim curl -y
RUN curl -L -O https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-7.10.1-linux-x86_64.tar.gz
RUN tar xzvf filebeat-7.10.1-linux-x86_64.tar.gz
ADD filebeat.yml /filebeat-7.10.1-linux-x86_64/filebeat.yml
RUN chmod go-w /filebeat-7.10.1-linux-x86_64/filebeat.yml
WORKDIR /opt
ADD target/staff-manager-api-*.jar staff-manager-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar",  "/opt/staff-manager-api.jar"]