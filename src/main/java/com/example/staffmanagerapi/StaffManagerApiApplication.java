package com.example.staffmanagerapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Staff Manager API",
                description = "vous pouvez faire un appel a http://localhost:8082/v3/api-docs ou l'URL docker pour recevoir un JSON, faites un copier coller dans fichier externe, avec l'extension .JSON, ensuite pour l'importez dans Postman > import > collection",
                contact = @Contact(
                        name = "M. Chekini",
                        url = "https://github.com/mchekini-check-consulting"
                ),
                version = "1.0.alpha"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8082",
                        description = "local : Intellij IDEA"
                ),
                @Server(
                        url = "http://staff-manager-api:8080",
                        description = "Remote docker"
                )
        }
)
public class StaffManagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaffManagerApiApplication.class, args);
    }

}
