#!/bin/bash

# Agent 1: Core Infrastructure Agent 작업 스크립트
echo "🤖 Agent 1: Core Infrastructure Agent 시작"
echo "=================================================="

# 색상 정의
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 프로젝트 루트 디렉토리
PROJECT_ROOT="/Users/gahee/spiritscribe-backend"
AGENT_DIR="$PROJECT_ROOT/agent1-core"

# Agent 1 전용 설정
AGENT_CONFIG='{
  "agent": {
    "name": "Core Infrastructure Agent",
    "role": "데이터베이스, 인증, 사용자 관리, 보안",
    "workspace": "agent1-core/",
    "dependencies": [],
    "outputs": ["사용자 도메인", "인증 시스템", "데이터베이스 스키마"]
  },
  "tasks": [
    "데이터베이스 스키마 설계 및 구현",
    "JWT 인증 시스템 구현", 
    "사용자 관리 API 구현",
    "보안 및 Rate Limiting 구현"
  ],
  "collaboration": {
    "notify": ["agent2", "agent3"],
    "wait_for": [],
    "deliverables": ["사용자 도메인 완성", "인증 시스템 완성"]
  }
}'

# 워크스페이스로 이동
cd "$AGENT_DIR"

echo -e "${BLUE}📁 작업 디렉토리: $AGENT_DIR${NC}"

# Agent 1 작업 시작
echo -e "${YELLOW}🚀 Agent 1 작업 시작...${NC}"

# 1. 데이터베이스 스키마 작업
echo -e "${BLUE}📊 1. 데이터베이스 스키마 작업${NC}"
mkdir -p database/migrations
mkdir -p database/schemas

# 사용자 관련 테이블 마이그레이션 생성
cat > database/migrations/V2__add_user_tables.sql << 'EOF'
-- 사용자 관련 테이블 추가
-- Agent 1: Core Infrastructure Agent 작업

-- 사용자 통계 테이블
CREATE TABLE IF NOT EXISTS user_stats (
    user_id VARCHAR(36) PRIMARY KEY,
    check_ins_count INT DEFAULT 0,
    followers_count INT DEFAULT 0,
    following_count INT DEFAULT 0,
    reviews_count INT DEFAULT 0,
    favorites_count INT DEFAULT 0,
    total_ratings_count INT DEFAULT 0,
    average_rating DECIMAL(3,2) DEFAULT 0.00,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_stats_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 팔로우 관계 테이블
CREATE TABLE IF NOT EXISTS follows (
    id VARCHAR(36) PRIMARY KEY,
    follower_id VARCHAR(36) NOT NULL,
    following_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_follow UNIQUE (follower_id, following_id),
    CONSTRAINT fk_follows_follower FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_follows_following FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 차단 관계 테이블
CREATE TABLE IF NOT EXISTS blocks (
    id VARCHAR(36) PRIMARY KEY,
    blocker_id VARCHAR(36) NOT NULL,
    blocked_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_block UNIQUE (blocker_id, blocked_id),
    CONSTRAINT fk_blocks_blocker FOREIGN KEY (blocker_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_blocks_blocked FOREIGN KEY (blocked_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_follows_follower ON follows(follower_id);
CREATE INDEX IF NOT EXISTS idx_follows_following ON follows(following_id);
CREATE INDEX IF NOT EXISTS idx_follows_created_at ON follows(created_at);
CREATE INDEX IF NOT EXISTS idx_blocks_blocker ON blocks(blocker_id);
CREATE INDEX IF NOT EXISTS idx_blocks_blocked ON blocks(blocked_id);
EOF

# 2. JWT 인증 시스템 구현
echo -e "${BLUE}🔐 2. JWT 인증 시스템 구현${NC}"
mkdir -p auth/jwt
mkdir -p auth/security

# JWT 토큰 프로바이더
cat > auth/jwt/JwtTokenProvider.kt << 'EOF'
package com.spiritscribe.auth.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${security.jwt.secret}") private val secret: String,
    @Value("\${security.jwt.access-token-ttl-seconds}") private val accessTokenTtl: Long
) {
    
    private val key: Key = Keys.hmacShaKeyFor(secret.toByteArray())
    
    // JWT 토큰 생성
    fun generateToken(userId: String, username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + accessTokenTtl * 1000)
        
        return Jwts.builder()
            .setSubject(userId)
            .claim("username", username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }
    
    // JWT 토큰 검증
    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: JwtException) {
            false
        }
    }
    
    // JWT 토큰에서 사용자 ID 추출
    fun getUserIdFromToken(token: String): String {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        
        return claims.subject
    }
    
    // JWT 토큰에서 사용자명 추출
    fun getUsernameFromToken(token: String): String {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        
        return claims["username"] as String
    }
}
EOF

# 3. 사용자 관리 API 구현
echo -e "${BLUE}👤 3. 사용자 관리 API 구현${NC}"
mkdir -p user/api
mkdir -p user/service
mkdir -p user/repository

# 사용자 서비스 확장
cat > user/service/UserService.kt << 'EOF'
package com.spiritscribe.user.service

import com.spiritscribe.user.domain.User
import com.spiritscribe.user.repository.UserRepository
import com.spiritscribe.user.api.UserProfileResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
) {
    
    // 사용자 프로필 조회
    fun findUserProfileById(userId: String): UserProfileResponse? {
        val user = userRepository.findById(userId).orElse(null) ?: return null
        return UserProfileResponse(
            id = user.id,
            username = user.username,
            displayName = user.displayName,
            profileImageUrl = user.profileImageUrl,
            bio = user.bio,
            isVerified = user.isVerified,
            isPrivate = user.isPrivate,
            location = user.location,
            website = user.website,
            createdAt = user.createdAt
        )
    }
    
    // 사용자 프로필 업데이트
    fun updateUserProfile(userId: String, updateRequest: UserProfileUpdateRequest): UserProfileResponse? {
        val user = userRepository.findById(userId).orElse(null) ?: return null
        
        val updatedUser = user.copy(
            displayName = updateRequest.displayName ?: user.displayName,
            bio = updateRequest.bio ?: user.bio,
            location = updateRequest.location ?: user.location,
            website = updateRequest.website ?: user.website,
            isPrivate = updateRequest.isPrivate ?: user.isPrivate
        )
        
        val savedUser = userRepository.save(updatedUser)
        return findUserProfileById(savedUser.id)
    }
    
    // 사용자 검색
    fun searchUsers(query: String, limit: Int = 20): List<UserProfileResponse> {
        return userRepository.findByUsernameContainingIgnoreCaseOrDisplayNameContainingIgnoreCase(query, query)
            .take(limit)
            .map { user ->
                UserProfileResponse(
                    id = user.id,
                    username = user.username,
                    displayName = user.displayName,
                    profileImageUrl = user.profileImageUrl,
                    bio = user.bio,
                    isVerified = user.isVerified,
                    isPrivate = user.isPrivate,
                    location = user.location,
                    website = user.website,
                    createdAt = user.createdAt
                )
            }
    }
}

data class UserProfileUpdateRequest(
    val displayName: String? = null,
    val bio: String? = null,
    val location: String? = null,
    val website: String? = null,
    val isPrivate: Boolean? = null
)
EOF

# 4. 보안 및 Rate Limiting 구현
echo -e "${BLUE}🛡️ 4. 보안 및 Rate Limiting 구현${NC}"
mkdir -p security/ratelimit

# Rate Limiting 서비스
cat > security/ratelimit/RateLimitService.kt << 'EOF'
package com.spiritscribe.security.ratelimit

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class RateLimitService(
    private val redisTemplate: RedisTemplate<String, String>
) {
    
    // Rate Limiting 체크
    fun isAllowed(key: String, limit: Int, windowSeconds: Long): Boolean {
        val now = Instant.now()
        val windowStart = now.minusSeconds(windowSeconds)
        
        val count = redisTemplate.opsForZSet()
            .count(key, windowStart.epochSecond.toDouble(), now.epochSecond.toDouble())
        
        return count < limit
    }
    
    // Rate Limiting 기록
    fun recordRequest(key: String, windowSeconds: Long) {
        val now = Instant.now()
        val score = now.epochSecond.toDouble()
        
        redisTemplate.opsForZSet().add(key, now.toString(), score)
        redisTemplate.expire(key, Duration.ofSeconds(windowSeconds))
    }
    
    // Rate Limiting 키 생성
    fun generateKey(prefix: String, identifier: String): String {
        return "$prefix:$identifier"
    }
}
EOF

echo -e "${GREEN}✅ Agent 1 작업 완료${NC}"
echo -e "${YELLOW}📋 완성된 작업:${NC}"
echo "  - 데이터베이스 스키마 (사용자, 팔로우, 차단 테이블)"
echo "  - JWT 인증 시스템"
echo "  - 사용자 관리 API"
echo "  - 보안 및 Rate Limiting"

echo -e "${BLUE}🔔 Agent 2, Agent 3에게 알림: 사용자 도메인 완성${NC}"
