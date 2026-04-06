package com.finance.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger (OpenAPI) Configuration
 *
 * This class configures API documentation and enables JWT authentication support
 * within Swagger UI.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures OpenAPI documentation with JWT Bearer authentication.
     *
     * @return OpenAPI configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // API Metadata
                .info(new Info()
                        .title("Finance Backend API")
                        .version("1.0")
                        .description("Backend APIs for Finance Data Processing and Access Control System"))

                // Enable JWT authentication in Swagger UI
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))

                // Define security scheme
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("Authorization") // Header name
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}