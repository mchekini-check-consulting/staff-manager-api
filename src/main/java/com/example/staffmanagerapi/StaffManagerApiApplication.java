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
                description = "Sur Postman cliquez sur Import => coller cet url : 'http://check-consulting.net:8080/v3/api-docs' , ensuite choisir 'Postman Collection'",
                contact = @Contact(
                        name = "M. Chekini",
                        url = "https://github.com/mchekini-check-consulting"
                ),
                version = "1"
        ),
        servers = {
                @Server(
                        url = "http://check-consulting.net:8080",
                        description = "Serveur Environnement integration"
                ),
                @Server(
                        url = "http://localhost:8080",
                        description = "Serveur Environnement local : Intellij IDEA"
                )
        }
)
public class StaffManagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaffManagerApiApplication.class, args);
    }

}
