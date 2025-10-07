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
    
    @Column(name = "is_read", nullable = false)
    val isRead: Boolean = false,
    
    @Column(name = "created_at")
    val createdAt: Instant? = null
)

enum class NotificationType {
    LIKE, COMMENT, FOLLOW, MENTION, SHARE, FEATURED
}
