package com.ege.teams.configuration

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
open class RestTemplateConfiguration {

    @Bean
    open fun restTemplate(): RestTemplate {
        return RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(120))
            .setReadTimeout(Duration.ofSeconds(120))
            .build()
    }
}
