package com.spiritscribe.user.api

import org.springframework.http.ResponseEntity
import com.spiritscribe.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class UserProfileResponse(
    val id: String,
    val username: String,
    val displayName: String,
)

@RestController
@RequestMapping("/v1/users")
class UserController(
    private val userService: UserService,
) {

    // 사용자 ID로 간단한 프로필을 조회한다. 스켈레톤 단계에서는 더미 데이터를 반환한다.
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: String): ResponseEntity<UserProfileResponse> {
        val profile = userService.findUserProfileById(userId)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(profile)
    }
}


