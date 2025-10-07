package com.spiritscribe.health

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
@Tag(name = "Health", description = "헬스 체크 관련 API")
class HealthController {

    // 헬스 체크 엔드포인트. 배포 및 헬스 모니터링을 위한 간단한 상태 확인을 제공한다.
    @GetMapping("/health")
    @Operation(
        summary = "헬스 체크",
        description = "애플리케이션의 상태를 확인합니다. 정상 동작 시 'ok' 상태를 반환합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "서버가 정상 동작 중"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun health(): ResponseEntity<Map<String, String>> =
        ResponseEntity.ok(mapOf("status" to "ok"))
}


