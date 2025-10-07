package com.spiritscribe.admin.api

import com.spiritscribe.admin.service.AdminService
import com.spiritscribe.admin.service.FakeDataService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * 어드민 API 컨트롤러
 * 관리자 전용 API 엔드포인트를 제공한다.
 */
@RestController
@RequestMapping("/v1/admin")
@Tag(name = "Admin", description = "관리자 전용 API")
class AdminApiController(
    private val adminService: AdminService,
    private val fakeDataService: FakeDataService
) {

    /**
     * 시스템 통계 조회
     * 실시간 시스템 통계를 반환한다.
     */
    @GetMapping("/stats")
    @Operation(
        summary = "시스템 통계 조회",
        description = "사용자 수, 체크인 수, 위스키 수 등의 시스템 통계를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "통계 조회 성공"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun getSystemStats(): ResponseEntity<Map<String, Any>> {
        val stats = adminService.getSystemStats()
        return ResponseEntity.ok(stats)
    }

    /**
     * 시스템 상태 확인
     * 데이터베이스, Redis, 외부 서비스 상태를 확인한다.
     */
    @GetMapping("/health")
    @Operation(
        summary = "시스템 상태 확인",
        description = "데이터베이스, Redis, 외부 서비스의 상태를 확인합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "시스템 상태 확인 성공"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun getSystemHealth(): ResponseEntity<Map<String, Any>> {
        val health = adminService.checkSystemHealth()
        return ResponseEntity.ok(health)
    }

    /**
     * 가짜 사용자 생성
     * 지정된 수만큼 가짜 사용자를 생성한다.
     */
    @PostMapping("/fake-data/users")
    @Operation(
        summary = "가짜 사용자 생성",
        description = "테스트용 가짜 사용자를 생성합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "가짜 사용자 생성 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun generateFakeUsers(
        @Parameter(description = "생성할 사용자 수", required = true)
        @RequestParam count: Int
    ): ResponseEntity<Map<String, Any>> {
        if (count <= 0 || count > 1000) {
            return ResponseEntity.badRequest().body(
                mapOf("error" to "사용자 수는 1-1000 사이여야 합니다.")
            )
        }
        
        val result = fakeDataService.generateFakeUsers(count)
        return ResponseEntity.ok(
            mapOf(
                "success" to true,
                "message" to "${result}명의 가짜 사용자가 생성되었습니다.",
                "count" to result
            )
        )
    }

    /**
     * 가짜 체크인 생성
     * 지정된 수만큼 가짜 체크인을 생성한다.
     */
    @PostMapping("/fake-data/checkins")
    @Operation(
        summary = "가짜 체크인 생성",
        description = "테스트용 가짜 체크인을 생성합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "가짜 체크인 생성 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun generateFakeCheckIns(
        @Parameter(description = "생성할 체크인 수", required = true)
        @RequestParam count: Int
    ): ResponseEntity<Map<String, Any>> {
        if (count <= 0 || count > 1000) {
            return ResponseEntity.badRequest().body(
                mapOf("error" to "체크인 수는 1-1000 사이여야 합니다.")
            )
        }
        
        val result = fakeDataService.generateFakeCheckIns(count)
        return ResponseEntity.ok(
            mapOf(
                "success" to true,
                "message" to "${result}개의 가짜 체크인이 생성되었습니다.",
                "count" to result
            )
        )
    }

    /**
     * 가짜 위스키 생성
     * 지정된 수만큼 가짜 위스키를 생성한다.
     */
    @PostMapping("/fake-data/whiskies")
    @Operation(
        summary = "가짜 위스키 생성",
        description = "테스트용 가짜 위스키를 생성합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "가짜 위스키 생성 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun generateFakeWhiskies(
        @Parameter(description = "생성할 위스키 수", required = true)
        @RequestParam count: Int
    ): ResponseEntity<Map<String, Any>> {
        if (count <= 0 || count > 1000) {
            return ResponseEntity.badRequest().body(
                mapOf("error" to "위스키 수는 1-1000 사이여야 합니다.")
            )
        }
        
        val result = fakeDataService.generateFakeWhiskies(count)
        return ResponseEntity.ok(
            mapOf(
                "success" to true,
                "message" to "${result}개의 가짜 위스키가 생성되었습니다.",
                "count" to result
            )
        )
    }

    /**
     * 종합 가짜 데이터 생성
     * 모든 유형의 가짜 데이터를 한 번에 생성한다.
     */
    @PostMapping("/fake-data/all")
    @Operation(
        summary = "종합 가짜 데이터 생성",
        description = "모든 유형의 가짜 데이터를 한 번에 생성합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "가짜 데이터 생성 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun generateAllFakeData(
        @Parameter(description = "생성할 사용자 수", required = true)
        @RequestParam userCount: Int,
        @Parameter(description = "생성할 체크인 수", required = true)
        @RequestParam checkInCount: Int,
        @Parameter(description = "생성할 위스키 수", required = true)
        @RequestParam whiskeyCount: Int
    ): ResponseEntity<Map<String, Any>> {
        if (userCount < 0 || userCount > 1000 || 
            checkInCount < 0 || checkInCount > 1000 || 
            whiskeyCount < 0 || whiskeyCount > 1000) {
            return ResponseEntity.badRequest().body(
                mapOf("error" to "모든 수는 0-1000 사이여야 합니다.")
            )
        }
        
        val results = fakeDataService.generateAllFakeData(userCount, checkInCount, whiskeyCount)
        return ResponseEntity.ok(
            mapOf(
                "success" to true,
                "message" to "모든 가짜 데이터가 생성되었습니다.",
                "results" to results
            )
        )
    }

    /**
     * 데이터 초기화
     * 모든 가짜 데이터를 삭제한다.
     */
    @DeleteMapping("/fake-data")
    @Operation(
        summary = "데이터 초기화",
        description = "모든 가짜 데이터를 삭제합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "데이터 초기화 성공"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun resetAllData(): ResponseEntity<Map<String, Any>> {
        val result = adminService.resetAllData()
        return ResponseEntity.ok(
            mapOf(
                "success" to true,
                "message" to "모든 가짜 데이터가 삭제되었습니다.",
                "deletedCount" to result
            )
        )
    }

    /**
     * 사용자 목록 조회
     * 관리자가 사용자 목록을 조회할 수 있다.
     */
    @GetMapping("/users")
    @Operation(
        summary = "사용자 목록 조회",
        description = "모든 사용자의 목록을 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun getAllUsers(): ResponseEntity<List<Map<String, Any>>> {
        val users = adminService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    /**
     * 체크인 목록 조회
     * 관리자가 체크인 목록을 조회할 수 있다.
     */
    @GetMapping("/checkins")
    @Operation(
        summary = "체크인 목록 조회",
        description = "모든 체크인의 목록을 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "체크인 목록 조회 성공"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun getAllCheckIns(): ResponseEntity<List<Map<String, Any>>> {
        val checkins = adminService.getAllCheckIns()
        return ResponseEntity.ok(checkins)
    }
}
