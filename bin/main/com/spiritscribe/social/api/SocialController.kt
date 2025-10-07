package com.spiritscribe.social.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

data class LikeResponse(
    val id: String,
    val userId: String,
    val checkInId: String?,
    val commentId: String?,
    val createdAt: Instant
)

data class CommentResponse(
    val id: String,
    val checkInId: String,
    val userId: String,
    val content: String,
    val parentCommentId: String?,
    val likesCount: Int = 0,
    val repliesCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val createdAt: Instant,
    val user: UserInfo? = null
)

data class UserInfo(
    val id: String,
    val username: String,
    val displayName: String,
    val profileImageUrl: String?,
    val isVerified: Boolean = false
)

data class CommentCreateRequest(
    val content: String,
    val parentCommentId: String? = null
)

data class CommentUpdateRequest(
    val content: String
)

data class ShareResponse(
    val id: String,
    val userId: String,
    val checkInId: String,
    val platform: String,
    val createdAt: Instant
)

data class ShareRequest(
    val platform: String // "FACEBOOK", "TWITTER", "INSTAGRAM", "KAKAO"
)

@RestController
@RequestMapping("/v1")
@Tag(name = "Social", description = "소셜 인터랙션 관련 API")
class SocialController {

    @PostMapping("/check-ins/{checkInId}/like")
    @Operation(
        summary = "체크인 좋아요",
        description = "체크인에 좋아요를 추가합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "좋아요 추가 성공"),
            ApiResponse(responseCode = "400", description = "이미 좋아요한 상태"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "404", description = "체크인을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun likeCheckIn(
        @Parameter(description = "체크인 ID", required = true)
        @PathVariable checkInId: String
    ): ResponseEntity<LikeResponse> {
        // 실제 구현에서는 좋아요 추가 로직 수행
        val like = LikeResponse(
            id = "like_${System.currentTimeMillis()}",
            userId = "current-user-id",
            checkInId = checkInId,
            commentId = null,
            createdAt = Instant.now()
        )
        return ResponseEntity.ok(like)
    }

    @DeleteMapping("/check-ins/{checkInId}/like")
    @Operation(
        summary = "체크인 좋아요 취소",
        description = "체크인의 좋아요를 취소합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "좋아요 취소 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "404", description = "좋아요를 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun unlikeCheckIn(
        @Parameter(description = "체크인 ID", required = true)
        @PathVariable checkInId: String
    ): ResponseEntity<Void> {
        // 실제 구현에서는 좋아요 취소 로직 수행
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/check-ins/{checkInId}/comments")
    @Operation(
        summary = "댓글 추가",
        description = "체크인에 댓글을 추가합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "댓글 추가 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "404", description = "체크인을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun addComment(
        @Parameter(description = "체크인 ID", required = true)
        @PathVariable checkInId: String,
        @RequestBody request: CommentCreateRequest
    ): ResponseEntity<CommentResponse> {
        // 실제 구현에서는 댓글 추가 로직 수행
        val comment = CommentResponse(
            id = "comment_${System.currentTimeMillis()}",
            checkInId = checkInId,
            userId = "current-user-id",
            content = request.content,
            parentCommentId = request.parentCommentId,
            likesCount = 0,
            repliesCount = 0,
            isLikedByMe = false,
            createdAt = Instant.now(),
            user = UserInfo(
                id = "current-user-id",
                username = "whiskeylover",
                displayName = "위스키러버",
                profileImageUrl = null,
                isVerified = false
            )
        )
        return ResponseEntity.ok(comment)
    }

    @GetMapping("/check-ins/{checkInId}/comments")
    @Operation(
        summary = "댓글 목록 조회",
        description = "체크인의 댓글 목록을 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공"),
            ApiResponse(responseCode = "404", description = "체크인을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun getComments(
        @Parameter(description = "체크인 ID", required = true)
        @PathVariable checkInId: String,
        @Parameter(description = "페이지 크기", example = "20")
        @RequestParam(defaultValue = "20") limit: Int,
        @Parameter(description = "오프셋", example = "0")
        @RequestParam(defaultValue = "0") offset: Int
    ): ResponseEntity<List<CommentResponse>> {
        // 실제 구현에서는 댓글 목록 조회 로직 수행
        val comments = listOf(
            CommentResponse(
                id = "comment_1",
                checkInId = checkInId,
                userId = "user_1",
                content = "정말 좋은 위스키네요! 어디서 구매하셨나요?",
                parentCommentId = null,
                likesCount = 5,
                repliesCount = 2,
                isLikedByMe = false,
                createdAt = Instant.now(),
                user = UserInfo(
                    id = "user_1",
                    username = "whiskeylover1",
                    displayName = "위스키러버1",
                    profileImageUrl = null,
                    isVerified = false
                )
            )
        )
        return ResponseEntity.ok(comments)
    }

    @PutMapping("/comments/{commentId}")
    @Operation(
        summary = "댓글 수정",
        description = "댓글 내용을 수정합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "403", description = "권한 없음"),
            ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun updateComment(
        @Parameter(description = "댓글 ID", required = true)
        @PathVariable commentId: String,
        @RequestBody request: CommentUpdateRequest
    ): ResponseEntity<CommentResponse> {
        // 실제 구현에서는 댓글 수정 로직 수행
        val comment = CommentResponse(
            id = commentId,
            checkInId = "checkin_1",
            userId = "current-user-id",
            content = request.content,
            parentCommentId = null,
            likesCount = 5,
            repliesCount = 2,
            isLikedByMe = false,
            createdAt = Instant.now(),
            user = UserInfo(
                id = "current-user-id",
                username = "whiskeylover",
                displayName = "위스키러버",
                profileImageUrl = null,
                isVerified = false
            )
        )
        return ResponseEntity.ok(comment)
    }

    @DeleteMapping("/comments/{commentId}")
    @Operation(
        summary = "댓글 삭제",
        description = "댓글을 삭제합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "403", description = "권한 없음"),
            ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun deleteComment(
        @Parameter(description = "댓글 ID", required = true)
        @PathVariable commentId: String
    ): ResponseEntity<Void> {
        // 실제 구현에서는 댓글 삭제 로직 수행
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/check-ins/{checkInId}/share")
    @Operation(
        summary = "체크인 공유",
        description = "체크인을 외부 플랫폼에 공유합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "공유 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "404", description = "체크인을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun shareCheckIn(
        @Parameter(description = "체크인 ID", required = true)
        @PathVariable checkInId: String,
        @RequestBody request: ShareRequest
    ): ResponseEntity<ShareResponse> {
        // 실제 구현에서는 공유 로직 수행
        val share = ShareResponse(
            id = "share_${System.currentTimeMillis()}",
            userId = "current-user-id",
            checkInId = checkInId,
            platform = request.platform,
            createdAt = Instant.now()
        )
        return ResponseEntity.ok(share)
    }
}
