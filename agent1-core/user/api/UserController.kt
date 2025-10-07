package com.spiritscribe.user.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/v1/users")
class UserController {
    
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: String): ResponseEntity<UserProfileResponse> {
        // 사용자 프로필 조회 로직
        val profile = UserProfileResponse(
            id = userId,
            username = "whiskeylover",
            displayName = "위스키러버",
            profileImageUrl = null,
            bio = "위스키를 사랑하는 사람입니다",
            isVerified = false,
            isPrivate = false,
            location = "서울, 한국",
            website = null,
            createdAt = Instant.now()
        )
        return ResponseEntity.ok(profile)
    }
    
    @PutMapping("/me")
    fun updateProfile(@RequestBody request: UserProfileUpdateRequest): ResponseEntity<UserProfileResponse> {
        // 프로필 업데이트 로직
        return ResponseEntity.ok(UserProfileResponse(
            id = "user123",
            username = request.username ?: "whiskeylover",
            displayName = request.displayName ?: "위스키러버",
            profileImageUrl = null,
            bio = request.bio,
            isVerified = false,
            isPrivate = request.isPrivate ?: false,
            location = request.location,
            website = request.website,
            createdAt = Instant.now()
        ))
    }
}

data class UserProfileResponse(
    val id: String,
    val username: String,
    val displayName: String,
    val profileImageUrl: String?,
    val bio: String?,
    val isVerified: Boolean,
    val isPrivate: Boolean,
    val location: String?,
    val website: String?,
    val createdAt: Instant?
)

data class UserProfileUpdateRequest(
    val username: String? = null,
    val displayName: String? = null,
    val bio: String? = null,
    val location: String? = null,
    val website: String? = null,
    val isPrivate: Boolean? = null
)
