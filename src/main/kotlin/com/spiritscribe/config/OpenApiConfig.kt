package com.spiritscribe.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    // OpenAPI 3.0 설정을 구성한다. Swagger UI에서 API 문서를 확인할 수 있도록 한다.
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("SpiritScribe Backend API")
                    .description("위스키 소셜 커뮤니티 SpiritScribe의 백엔드 API 문서입니다.")
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("SpiritScribe Team")
                            .email("contact@spiritscribe.com")
                            .url("https://spiritscribe.com")
                    )
                    .license(
                        License()
                            .name("MIT License")
                            .url("https://opensource.org/licenses/MIT")
                    )
            )
            .addServersItem(
                Server()
                    .url("http://localhost:8080")
                    .description("로컬 개발 서버")
            )
            .addServersItem(
                Server()
                    .url("https://dev-api.spiritscribe.com")
                    .description("개발 서버")
            )
            .addServersItem(
                Server()
                    .url("https://api.spiritscribe.com")
                    .description("프로덕션 서버")
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearerAuth",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("JWT 토큰을 입력하세요. 예: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                    )
            )
            .addSecurityItem(
                SecurityRequirement().addList("bearerAuth")
            )
    }
}
