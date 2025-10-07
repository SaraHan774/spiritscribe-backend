package com.spiritscribe.user.service

import com.spiritscribe.user.api.UserProfileResponse
import com.spiritscribe.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    // 사용자 ID로 프로필 응답을 조회한다. 존재하지 않을 경우 null을 반환한다.
    fun findUserProfileById(userId: String): UserProfileResponse? {
        val user = userRepository.findById(userId).orElse(null) ?: return null
        return UserProfileResponse(
            id = user.id,
            username = user.username,
            displayName = user.displayName,
        )
    }
}


