package com.spiritscribe.auth.api

import com.spiritscribe.auth.service.JwtTokenService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String,
    val displayName: String,
    val bio: String? = null,
    val location: String? = null
)

data class SignInRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val user: UserInfo
)

data class UserInfo(
    val id: String,
    val username: String,
    val email: String,
    val displayName: String,
    val profileImageUrl: String? = null,
    val bio: String? = null,
    val isVerified: Boolean = false,
    val isPrivate: Boolean = false,
    val location: String? = null,
    val website: String? = null,
    val createdAt: Instant
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class PasswordResetRequest(
    val email: String
)

data class PasswordResetConfirmRequest(
    val token: String,
    val newPassword: String
)

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Authentication", description = "인증 관련 API")
class AuthController(
    private val jwtTokenService: JwtTokenService
) {

    @PostMapping("/signup")
    @Operation(
        summary = "회원가입",
        description = "새로운 사용자 계정을 생성합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "회원가입 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "409", description = "이미 존재하는 이메일 또는 사용자명"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun signUp(
        @RequestBody request: SignUpRequest
    ): ResponseEntity<AuthResponse> {
        // 실제 구현에서는 사용자 생성 로직 수행
        val userId = "user_${System.currentTimeMillis()}"
        val accessToken = jwtTokenService.generateToken(userId, request.username)
        
        val authResponse = AuthResponse(
            accessToken = accessToken,
            expiresIn = 3600, // 1시간
            user = UserInfo(
                id = userId,
                username = request.username,
                email = request.email,
                displayName = request.displayName,
                profileImageUrl = null,
                bio = request.bio,
                isVerified = false,
                isPrivate = false,
                location = request.location,
                website = null,
                createdAt = Instant.now()
            )
        )
        
        return ResponseEntity.ok(authResponse)
    }

    @PostMapping("/signin")
    @Operation(
        summary = "로그인",
        description = "이메일과 비밀번호로 로그인합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "로그인 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "401", description = "인증 실패"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun signIn(
        @RequestBody request: SignInRequest
    ): ResponseEntity<AuthResponse> {
        // 실제 구현에서는 이메일/비밀번호 검증 로직 수행
        val userId = "user_123"
        val username = "whiskeylover"
        val accessToken = jwtTokenService.generateToken(userId, username)
        
        val authResponse = AuthResponse(
            accessToken = accessToken,
            expiresIn = 3600, // 1시간
            user = UserInfo(
                id = userId,
                username = username,
                email = request.email,
                displayName = "위스키러버",
                profileImageUrl = null,
                bio = "위스키를 사랑하는 사람입니다",
                isVerified = false,
                isPrivate = false,
                location = "서울, 한국",
                website = null,
                createdAt = Instant.now()
            )
        )
        
        return ResponseEntity.ok(authResponse)
    }

    @PostMapping("/refresh")
    @Operation(
        summary = "토큰 갱신",
        description = "리프레시 토큰으로 새로운 액세스 토큰을 발급받습니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "토큰 갱신 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun refreshToken(
        @RequestBody request: RefreshTokenRequest
    ): ResponseEntity<AuthResponse> {
        // 실제 구현에서는 리프레시 토큰 검증 로직 수행
        val userId = "user_123"
        val username = "whiskeylover"
        val accessToken = jwtTokenService.generateToken(userId, username)
        
        val authResponse = AuthResponse(
            accessToken = accessToken,
            expiresIn = 3600, // 1시간
            user = UserInfo(
                id = userId,
                username = username,
                email = "whiskeylover@example.com",
                displayName = "위스키러버",
                profileImageUrl = null,
                bio = "위스키를 사랑하는 사람입니다",
                isVerified = false,
                isPrivate = false,
                location = "서울, 한국",
                website = null,
                createdAt = Instant.now()
            )
        )
        
        return ResponseEntity.ok(authResponse)
    }

    @PostMapping("/logout")
    @Operation(
        summary = "로그아웃",
        description = "사용자를 로그아웃합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    @SecurityRequirement(name = "bearerAuth")
    fun logout(): ResponseEntity<Map<String, String>> {
        // 실제 구현에서는 토큰 무효화 로직 수행
        return ResponseEntity.ok(mapOf("message" to "로그아웃되었습니다"))
    }

    @PostMapping("/password-reset")
    @Operation(
        summary = "비밀번호 재설정 요청",
        description = "비밀번호 재설정을 위한 이메일을 발송합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "비밀번호 재설정 이메일 발송 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "404", description = "존재하지 않는 이메일"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun requestPasswordReset(
        @RequestBody request: PasswordResetRequest
    ): ResponseEntity<Map<String, String>> {
        // 실제 구현에서는 비밀번호 재설정 이메일 발송 로직 수행
        return ResponseEntity.ok(mapOf("message" to "비밀번호 재설정 이메일이 발송되었습니다"))
    }

    @PostMapping("/password-reset/confirm")
    @Operation(
        summary = "비밀번호 재설정 확인",
        description = "비밀번호 재설정 토큰으로 새 비밀번호를 설정합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "비밀번호 재설정 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "401", description = "유효하지 않은 토큰"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun confirmPasswordReset(
        @RequestBody request: PasswordResetConfirmRequest
    ): ResponseEntity<Map<String, String>> {
        // 실제 구현에서는 비밀번호 재설정 확인 로직 수행
        return ResponseEntity.ok(mapOf("message" to "비밀번호가 성공적으로 재설정되었습니다"))
    }

    @GetMapping("/me")
    @Operation(
        summary = "내 정보 조회",
        description = "현재 로그인한 사용자의 정보를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    @SecurityRequirement(name = "bearerAuth")
    fun getMe(): ResponseEntity<UserInfo> {
        // 실제 구현에서는 JWT 토큰에서 사용자 정보 추출
        val userInfo = UserInfo(
            id = "user_123",
            username = "whiskeylover",
            email = "whiskeylover@example.com",
            displayName = "위스키러버",
            profileImageUrl = null,
            bio = "위스키를 사랑하는 사람입니다",
            isVerified = false,
            isPrivate = false,
            location = "서울, 한국",
            website = null,
            createdAt = Instant.now()
        )
        
        return ResponseEntity.ok(userInfo)
    }
}
