package com.novavista.binaa.center.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:Binaa Center}")
    private String applicationName;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Bean
    public OpenAPI binaaCenterOpenAPI() {
        final String securitySchemeName = "JWT Authentication";

        // Create Security Scheme
        SecurityScheme securityScheme = new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter JWT token")
                .in(SecurityScheme.In.HEADER);

        // Create Security Requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(securitySchemeName);

        // Create Contact Information
        Contact contact = new Contact()
                .name("Binaa Center Support")
                .email("support@binaacenter.com")
                .url("https://www.binaacenter.com");

        return new OpenAPI()
                .info(new Info()
                        .title(applicationName + " API")
                        .description("REST API documentation for Binaa Center management system")
                        .version("1.0.0")
                        .contact(contact)
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Binaa Center Additional Documentation")
                        .url("https://docs.binaacenter.com"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080" + contextPath)
                                .description("Development Server"),
                        new Server()
                                .url("https://api.binaacenter.com" + contextPath)
                                .description("Production Server")))
                .addSecurityItem(securityRequirement)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, securityScheme))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName));
    }
}