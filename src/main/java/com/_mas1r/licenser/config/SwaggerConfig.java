package com._mas1r.licenser.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Licenser web application Management",
                version = "1.0.0",
                description = "Aplicacion web para creacion, manejo y gestion de licencias, la cual permite bloquear acceso a ciertas funcionalidades de la aplicaciones web y sitios web y genera notificaciones de renovacion de servicios de renovacion",
                contact = @Contact(
                        name = "Kerack Diaz",
                        email = "3mas1r@gmail.com"

                ))
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")
public class SwaggerConfig {
}
