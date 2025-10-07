package com.spiritscribe.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig(
    @Value("\${cors.allowed-origins}") private val allowedOrigins: String,
    @Value("\${cors.allowed-methods}") private val allowedMethods: String,
    @Value("\${cors.allowed-headers}") private val allowedHeaders: String,
    @Value("\${cors.allow-credentials}") private val allowCredentials: Boolean,
) {

    // CORS 설정을 구성한다. 프런트엔드 도메인과의 통신 허용을 위해 사용한다.
    @Bean
    fun corsFilter(): CorsFilter {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = allowedOrigins.split(",").map { it.trim() }
        configuration.allowedMethods = allowedMethods.split(",").map { it.trim() }
        configuration.allowedHeaders = allowedHeaders.split(",").map { it.trim() }
        configuration.allowCredentials = allowCredentials
        configuration.maxAge = 3600

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return CorsFilter(source)
    }
}


