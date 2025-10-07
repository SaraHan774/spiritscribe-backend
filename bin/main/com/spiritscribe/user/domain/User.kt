package com.spiritscribe.user.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "users")
class User(
    // 사용자 고유 식별자 (UUID 문자열 형태)
    @Id
    @Column(name = "id", nullable = false, length = 36)
    val id: String,

    // 고유 사용자명
    @Column(name = "username", nullable = false, length = 50, unique = true)
    val username: String,

    // 사용자 표시명
    @Column(name = "display_name", nullable = false, length = 100)
    val displayName: String,

    // 이메일 주소
    @Column(name = "email", nullable = false, length = 255, unique = true)
    val email: String,

    // 프로필 이미지 URL
    @Column(name = "profile_image_url", length = 500)
    val profileImageUrl: String? = null,

    // 자기소개
    @Column(name = "bio")
    val bio: String? = null,

    // 인증 사용자 여부
    @Column(name = "is_verified", nullable = false)
    val isVerified: Boolean = false,

    // 비공개 계정 여부
    @Column(name = "is_private", nullable = false)
    val isPrivate: Boolean = false,

    // 위치 정보
    @Column(name = "location", length = 100)
    val location: String? = null,

    // 웹사이트 URL
    @Column(name = "website", length = 255)
    val website: String? = null,

    // 생성/수정/마지막 로그인 시간 (애플리케이션에서는 읽기 전용으로 취급)
    @Column(name = "created_at")
    val createdAt: Instant? = null,

    @Column(name = "updated_at")
    val updatedAt: Instant? = null,

    @Column(name = "last_login_at")
    val lastLoginAt: Instant? = null,
)


