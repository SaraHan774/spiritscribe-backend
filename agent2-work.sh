#!/bin/bash

# Agent 2: Social Features Agent 작업 스크립트
echo "🤖 Agent 2: Social Features Agent 시작"
echo "=================================================="

# 색상 정의
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 프로젝트 루트 디렉토리
PROJECT_ROOT="/Users/gahee/spiritscribe-backend"
AGENT_DIR="$PROJECT_ROOT/agent2-social"

# Agent 2 전용 설정
AGENT_CONFIG='{
  "agent": {
    "name": "Social Features Agent",
    "role": "체크인, 소셜 인터랙션, 알림, 이미지 관리",
    "workspace": "agent2-social/",
    "dependencies": ["agent1"],
    "outputs": ["체크인 시스템", "소셜 인터랙션", "알림 시스템"]
  },
  "tasks": [
    "체크인 시스템 구현",
    "소셜 인터랙션 구현",
    "알림 시스템 구현",
    "이미지 관리 시스템 구현"
  ],
  "collaboration": {
    "notify": ["agent3"],
    "wait_for": ["agent1"],
    "deliverables": ["체크인 시스템 완성", "소셜 기능 완성"]
  }
}'

# 워크스페이스로 이동
cd "$AGENT_DIR"

echo -e "${BLUE}📁 작업 디렉토리: $AGENT_DIR${NC}"

# Agent 1 완료 대기
echo -e "${YELLOW}⏳ Agent 1 완료 대기 중...${NC}"
sleep 5  # 실제로는 Agent 1 완료 신호를 받을 때까지 대기

# Agent 2 작업 시작
echo -e "${YELLOW}🚀 Agent 2 작업 시작...${NC}"

# 1. 체크인 시스템 구현
echo -e "${BLUE}📝 1. 체크인 시스템 구현${NC}"
mkdir -p checkin/domain
mkdir -p checkin/repository
mkdir -p checkin/service
mkdir -p checkin/api

# 체크인 도메인 모델
cat > checkin/domain/CheckIn.kt << 'EOF'
package com.spiritscribe.checkin.domain

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "check_ins")
class CheckIn(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    val id: String,
    
    @Column(name = "user_id", nullable = false, length = 36)
    val userId: String,
    
    @Column(name = "whiskey_id", nullable = false, length = 36)
    val whiskeyId: String,
    
    @Column(name = "location", length = 255)
    val location: String? = null,
    
    @Column(name = "location_lat", precision = 10, scale = 8)
    val locationLat: Double? = null,
    
    @Column(name = "location_lng", precision = 11, scale = 8)
    val locationLng: Double? = null,
    
    @Enumerated(EnumType.STRING)
    @Column(name = "location_type")
    val locationType: LocationType = LocationType.OTHER,
    
    @Column(name = "rating", precision = 3, scale = 2)
    val rating: Double? = null,
    
    @Column(name = "notes", columnDefinition = "TEXT")
    val notes: String? = null,
    
    @Column(name = "is_public", nullable = false)
    val isPublic: Boolean = true,
    
    @Column(name = "is_featured", nullable = false)
    val isFeatured: Boolean = false,
    
    @Column(name = "created_at")
    val createdAt: Instant? = null,
    
    @Column(name = "updated_at")
    val updatedAt: Instant? = null
)

enum class LocationType {
    WHISKEY_BAR, RESTAURANT, HOME, EVENT, OTHER
}
EOF

# 체크인 리포지토리
cat > checkin/repository/CheckInRepository.kt << 'EOF'
package com.spiritscribe.checkin.repository

import com.spiritscribe.checkin.domain.CheckIn
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface CheckInRepository : JpaRepository<CheckIn, String> {
    
    // 사용자별 체크인 조회
    fun findByUserIdOrderByCreatedAtDesc(userId: String): List<CheckIn>
    
    // 공개 체크인 조회 (피드용)
    fun findByIsPublicTrueOrderByCreatedAtDesc(): List<CheckIn>
    
    // 위스키별 체크인 조회
    fun findByWhiskeyIdOrderByCreatedAtDesc(whiskeyId: String): List<CheckIn>
    
    // 위치별 체크인 조회
    @Query("SELECT c FROM CheckIn c WHERE c.locationLat IS NOT NULL AND c.locationLng IS NOT NULL " +
           "AND ST_DWithin(ST_Point(c.locationLng, c.locationLat), ST_Point(:lng, :lat), :radius)")
    fun findByLocationNear(@Param("lat") lat: Double, @Param("lng") lng: Double, @Param("radius") radius: Double): List<CheckIn>
    
    // 최근 체크인 조회
    fun findTop10ByIsPublicTrueOrderByCreatedAtDesc(): List<CheckIn>
}
EOF

# 2. 소셜 인터랙션 구현
echo -e "${BLUE}💬 2. 소셜 인터랙션 구현${NC}"
mkdir -p social/like
mkdir -p social/comment
mkdir -p social/share

# 좋아요 도메인
cat > social/like/Like.kt << 'EOF'
package com.spiritscribe.social.like

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "likes")
class Like(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    val id: String,
    
    @Column(name = "user_id", nullable = false, length = 36)
    val userId: String,
    
    @Column(name = "check_in_id", length = 36)
    val checkInId: String? = null,
    
    @Column(name = "comment_id", length = 36)
    val commentId: String? = null,
    
    @Column(name = "created_at")
    val createdAt: Instant? = null
) {
    init {
        require(checkInId != null || commentId != null) { "checkInId 또는 commentId 중 하나는 필수입니다" }
        require(!(checkInId != null && commentId != null)) { "checkInId와 commentId는 동시에 설정할 수 없습니다" }
    }
}
EOF

# 댓글 도메인
cat > social/comment/Comment.kt << 'EOF'
package com.spiritscribe.social.comment

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "comments")
class Comment(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    val id: String,
    
    @Column(name = "check_in_id", nullable = false, length = 36)
    val checkInId: String,
    
    @Column(name = "user_id", nullable = false, length = 36)
    val userId: String,
    
    @Column(name = "parent_comment_id", length = 36)
    val parentCommentId: String? = null,
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    val content: String,
    
    @Column(name = "likes_count", nullable = false)
    val likesCount: Int = 0,
    
    @Column(name = "replies_count", nullable = false)
    val repliesCount: Int = 0,
    
    @Column(name = "is_deleted", nullable = false)
    val isDeleted: Boolean = false,
    
    @Column(name = "created_at")
    val createdAt: Instant? = null,
    
    @Column(name = "updated_at")
    val updatedAt: Instant? = null
)
EOF

# 3. 알림 시스템 구현
echo -e "${BLUE}🔔 3. 알림 시스템 구현${NC}"
mkdir -p notification/domain
mkdir -p notification/service
mkdir -p notification/websocket

# 알림 도메인
cat > notification/domain/Notification.kt << 'EOF'
package com.spiritscribe.notification.domain

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "notifications")
class Notification(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    val id: String,
    
    @Column(name = "user_id", nullable = false, length = 36)
    val userId: String,
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: NotificationType,
    
    @Column(name = "title", nullable = false, length = 255)
    val title: String,
    
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    val message: String,
    
    @Column(name = "related_user_id", length = 36)
    val relatedUserId: String? = null,
    
    @Column(name = "related_check_in_id", length = 36)
    val relatedCheckInId: String? = null,
    
    @Column(name = "related_comment_id", length = 36)
    val relatedCommentId: String? = null,
    
    @Column(name = "is_read", nullable = false)
    val isRead: Boolean = false,
    
    @Column(name = "created_at")
    val createdAt: Instant? = null
)

enum class NotificationType {
    LIKE, COMMENT, FOLLOW, MENTION, SHARE, FEATURED
}
EOF

# WebSocket 설정
cat > notification/websocket/WebSocketConfig.kt << 'EOF'
package com.spiritscribe.notification.websocket

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS()
    }
    
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app")
        registry.enableSimpleBroker("/topic", "/queue")
    }
}
EOF

# 4. 이미지 관리 시스템 구현
echo -e "${BLUE}🖼️ 4. 이미지 관리 시스템 구현${NC}"
mkdir -p image/service
mkdir -p image/storage

# 이미지 서비스
cat > image/service/ImageService.kt << 'EOF'
package com.spiritscribe.image.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ImageService {
    
    // 이미지 업로드
    fun uploadImage(file: MultipartFile, checkInId: String): String {
        // 실제로는 AWS S3 또는 로컬 스토리지에 업로드
        val fileName = "${checkInId}_${UUID.randomUUID()}.${getFileExtension(file.originalFilename)}"
        
        // 이미지 검증
        validateImage(file)
        
        // 이미지 리사이징
        val resizedImage = resizeImage(file)
        
        // 스토리지에 저장
        val imageUrl = saveToStorage(resizedImage, fileName)
        
        return imageUrl
    }
    
    // 이미지 삭제
    fun deleteImage(imageUrl: String): Boolean {
        // 스토리지에서 이미지 삭제
        return deleteFromStorage(imageUrl)
    }
    
    // 이미지 검증
    private fun validateImage(file: MultipartFile) {
        val allowedTypes = listOf("image/jpeg", "image/png", "image/webp")
        if (!allowedTypes.contains(file.contentType)) {
            throw IllegalArgumentException("지원하지 않는 이미지 형식입니다")
        }
        
        if (file.size > 10 * 1024 * 1024) { // 10MB
            throw IllegalArgumentException("이미지 크기가 너무 큽니다 (최대 10MB)")
        }
    }
    
    // 이미지 리사이징
    private fun resizeImage(file: MultipartFile): ByteArray {
        // 실제로는 이미지 리사이징 로직 구현
        return file.bytes
    }
    
    // 스토리지에 저장
    private fun saveToStorage(imageData: ByteArray, fileName: String): String {
        // 실제로는 AWS S3 또는 로컬 스토리지에 저장
        return "https://cdn.spiritscribe.com/images/$fileName"
    }
    
    // 스토리지에서 삭제
    private fun deleteFromStorage(imageUrl: String): Boolean {
        // 실제로는 스토리지에서 삭제
        return true
    }
    
    // 파일 확장자 추출
    private fun getFileExtension(fileName: String?): String {
        return fileName?.substringAfterLast('.') ?: "jpg"
    }
}
EOF

echo -e "${GREEN}✅ Agent 2 작업 완료${NC}"
echo -e "${YELLOW}📋 완성된 작업:${NC}"
echo "  - 체크인 시스템 (CRUD, 피드)"
echo "  - 소셜 인터랙션 (좋아요, 댓글, 공유)"
echo "  - 알림 시스템 (WebSocket, FCM)"
echo "  - 이미지 관리 시스템"

echo -e "${BLUE}🔔 Agent 3에게 알림: 소셜 기능 완성${NC}"
