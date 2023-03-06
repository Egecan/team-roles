package com.ege.teams.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SwaggerConfiguration {

    @Bean
    open fun api(): OpenAPI? {
        return OpenAPI()
            .info(
                Info().title("Team Roles app")
                    .description("Team Roles application for Tempo")
                    .version("v1.0.0")
                    .license(License().name("Apache 2.0").url("http://springdoc.org"))
            )
    }
}
