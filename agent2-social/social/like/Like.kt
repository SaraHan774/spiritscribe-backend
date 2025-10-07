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
