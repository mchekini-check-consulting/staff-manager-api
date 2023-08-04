package com.example.staffmanagerapi.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Staff Manager API",
                contact = @Contact(
                        name = "M. Chekini",
                        url = "https://github.com/mchekini-check-consulting"
                ),
                version = "1"
        ),
        servers = {
                @Server(
                        url = "http://qua-vm-1.check-consulting.net:8080",
                        description = "Serveur Environnement QUA - VM N°1"
                ),
                @Server(
                        url = "http://qua-vm-2.check-consulting.net:8080",
                        description = "Serveur Environnement QUA - VM N°2"
                )
        }
)
public class SwaggerConfiguration {
}
