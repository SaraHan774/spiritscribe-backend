package com.spiritscribe.user.api

import com.spiritscribe.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class UserProfileResponse(
    val id: String,
    val username: String,
    val displayName: String,
    val profileImageUrl: String? = null,
    val bio: String? = null,
    val isVerified: Boolean = false,
    val isPrivate: Boolean = false,
    val location: String? = null,
    val website: String? = null,
    val stats: UserStats? = null
)

data class UserStats(
    val checkInsCount: Int = 0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val reviewsCount: Int = 0,
    val favoritesCount: Int = 0,
    val averageRating: Double = 0.0
)

data class UserProfileUpdateRequest(
    val displayName: String? = null,
    val bio: String? = null,
    val location: String? = null,
    val website: String? = null,
    val isPrivate: Boolean? = null
)

@RestController
@RequestMapping("/v1/users")
@Tag(name = "User", description = "사용자 관련 API")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/{userId}")
    @Operation(
        summary = "사용자 프로필 조회",
        description = "특정 사용자의 프로필 정보를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "사용자 프로필 조회 성공"),
            ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun getUser(
        @Parameter(description = "사용자 ID", required = true)
        @PathVariable userId: String
    ): ResponseEntity<UserProfileResponse> {
        val profile = userService.findUserProfileById(userId)
            ?: return ResponseEntity.notFound().build()
        
        return ResponseEntity.ok(
            UserProfileResponse(
                id = profile.id,
                username = profile.username,
                displayName = profile.displayName,
                profileImageUrl = null,
                bio = "위스키를 사랑하는 사람입니다",
                isVerified = false,
                isPrivate = false,
                location = "서울, 한국",
                website = null,
                stats = UserStats(
                    checkInsCount = 156,
                    followersCount = 1240,
                    followingCount = 890,
                    reviewsCount = 98,
                    favoritesCount = 45,
                    averageRating = 4.2
                )
            )
        )
    }

    @GetMapping("/me")
    @Operation(
        summary = "내 프로필 조회",
        description = "현재 로그인한 사용자의 프로필 정보를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "내 프로필 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun getMyProfile(): ResponseEntity<UserProfileResponse> {
        // 실제 구현에서는 JWT 토큰에서 사용자 ID를 추출
        return getUser("current-user-id")
    }

    @PutMapping("/me")
    @Operation(
        summary = "내 프로필 수정",
        description = "현재 로그인한 사용자의 프로필 정보를 수정합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "프로필 수정 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun updateMyProfile(
        @RequestBody request: UserProfileUpdateRequest
    ): ResponseEntity<UserProfileResponse> {
        // 실제 구현에서는 프로필 업데이트 로직 수행
        return ResponseEntity.ok(
            UserProfileResponse(
                id = "current-user-id",
                username = "whiskeylover",
                displayName = request.displayName ?: "위스키러버",
                profileImageUrl = null,
                bio = request.bio ?: "위스키를 사랑하는 사람입니다",
                isVerified = false,
                isPrivate = request.isPrivate ?: false,
                location = request.location ?: "서울, 한국",
                website = request.website,
                stats = UserStats(
                    checkInsCount = 156,
                    followersCount = 1240,
                    followingCount = 890,
                    reviewsCount = 98,
                    favoritesCount = 45,
                    averageRating = 4.2
                )
            )
        )
    }
}


