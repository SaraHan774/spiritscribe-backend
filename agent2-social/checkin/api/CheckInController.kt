package com.spiritscribe.checkin.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

data class CheckInResponse(
    val id: String,
    val userId: String,
    val whiskeyId: String,
    val location: String?,
    val locationLat: Double?,
    val locationLng: Double?,
    val locationType: String,
    val rating: Double?,
    val notes: String?,
    val images: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val isPublic: Boolean = true,
    val isFeatured: Boolean = false,
    val createdAt: Instant?,
    val user: UserInfo? = null,
    val whiskey: WhiskeyInfo? = null
)

data class UserInfo(
    val id: String,
    val username: String,
    val displayName: String,
    val profileImageUrl: String?,
    val isVerified: Boolean = false
)

data class WhiskeyInfo(
    val id: String,
    val name: String,
    val distillery: String,
    val imageUrl: String?
)

data class CheckInCreateRequest(
    val whiskeyId: String,
    val location: String? = null,
    val locationLat: Double? = null,
    val locationLng: Double? = null,
    val locationType: String = "OTHER",
    val rating: Double? = null,
    val notes: String? = null,
    val images: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val isPublic: Boolean = true
)

data class CheckInUpdateRequest(
    val location: String? = null,
    val locationLat: Double? = null,
    val locationLng: Double? = null,
    val locationType: String? = null,
    val rating: Double? = null,
    val notes: String? = null,
    val images: List<String>? = null,
    val tags: List<String>? = null,
    val isPublic: Boolean? = null
)

@RestController
@RequestMapping("/v1/check-ins")
@Tag(name = "CheckIn", description = "체크인 관련 API")
class CheckInController {

    @PostMapping
    @Operation(
        summary = "체크인 생성",
        description = "새로운 위스키 체크인을 생성합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "체크인 생성 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun createCheckIn(
        @RequestBody request: CheckInCreateRequest
    ): ResponseEntity<CheckInResponse> {
        // 실제 구현에서는 체크인 생성 로직 수행
        val checkIn = CheckInResponse(
            id = "checkin_${System.currentTimeMillis()}",
            userId = "current-user-id",
            whiskeyId = request.whiskeyId,
            location = request.location,
            locationLat = request.locationLat,
            locationLng = request.locationLng,
            locationType = request.locationType,
            rating = request.rating,
            notes = request.notes,
            images = request.images,
            tags = request.tags,
            likesCount = 0,
            commentsCount = 0,
            sharesCount = 0,
            isLikedByMe = false,
            isPublic = request.isPublic,
            isFeatured = false,
            createdAt = Instant.now(),
            user = UserInfo(
                id = "current-user-id",
                username = "whiskeylover",
                displayName = "위스키러버",
                profileImageUrl = null,
                isVerified = false
            ),
            whiskey = WhiskeyInfo(
                id = request.whiskeyId,
                name = "Macallan 18",
                distillery = "The Macallan",
                imageUrl = null
            )
        )
        return ResponseEntity.ok(checkIn)
    }

    @GetMapping("/{checkInId}")
    @Operation(
        summary = "체크인 조회",
        description = "특정 체크인의 상세 정보를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "체크인 조회 성공"),
            ApiResponse(responseCode = "404", description = "체크인을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun getCheckIn(
        @Parameter(description = "체크인 ID", required = true)
        @PathVariable checkInId: String
    ): ResponseEntity<CheckInResponse> {
        // 실제 구현에서는 체크인 조회 로직 수행
        val checkIn = CheckInResponse(
            id = checkInId,
            userId = "user_123",
            whiskeyId = "whiskey_456",
            location = "강남 위스키바",
            locationLat = 37.5665,
            locationLng = 126.9780,
            locationType = "WHISKEY_BAR",
            rating = 4.5,
            notes = "정말 부드럽고 복잡한 맛이에요. 오크 향이 특히 좋습니다!",
            images = listOf(
                "https://cdn.spiritscribe.com/checkins/image1.jpg",
                "https://cdn.spiritscribe.com/checkins/image2.jpg"
            ),
            tags = listOf("싱글몰트", "스모키", "프리미엄"),
            likesCount = 24,
            commentsCount = 8,
            sharesCount = 3,
            isLikedByMe = false,
            isPublic = true,
            isFeatured = false,
            createdAt = Instant.now(),
            user = UserInfo(
                id = "user_123",
                username = "whiskeylover",
                displayName = "위스키러버",
                profileImageUrl = "https://cdn.spiritscribe.com/profiles/user_123.jpg",
                isVerified = false
            ),
            whiskey = WhiskeyInfo(
                id = "whiskey_456",
                name = "Macallan 18",
                distillery = "The Macallan",
                imageUrl = "https://cdn.spiritscribe.com/whiskies/whiskey_456.jpg"
            )
        )
        return ResponseEntity.ok(checkIn)
    }

    @GetMapping
    @Operation(
        summary = "체크인 피드 조회",
        description = "공개된 체크인들의 피드를 조회합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "피드 조회 성공"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun getCheckInFeed(
        @Parameter(description = "페이지 크기", example = "20")
        @RequestParam(defaultValue = "20") limit: Int,
        @Parameter(description = "오프셋", example = "0")
        @RequestParam(defaultValue = "0") offset: Int
    ): ResponseEntity<List<CheckInResponse>> {
        // 실제 구현에서는 피드 조회 로직 수행
        val feed = listOf(
            CheckInResponse(
                id = "checkin_1",
                userId = "user_1",
                whiskeyId = "whiskey_1",
                location = "강남 위스키바",
                locationLat = 37.5665,
                locationLng = 126.9780,
                locationType = "WHISKEY_BAR",
                rating = 4.5,
                notes = "정말 부드럽고 복잡한 맛이에요!",
                images = emptyList(),
                tags = listOf("싱글몰트", "스모키"),
                likesCount = 24,
                commentsCount = 8,
                sharesCount = 3,
                isLikedByMe = false,
                isPublic = true,
                isFeatured = false,
                createdAt = Instant.now(),
                user = UserInfo(
                    id = "user_1",
                    username = "whiskeylover1",
                    displayName = "위스키러버1",
                    profileImageUrl = null,
                    isVerified = false
                ),
                whiskey = WhiskeyInfo(
                    id = "whiskey_1",
                    name = "Macallan 18",
                    distillery = "The Macallan",
                    imageUrl = null
                )
            )
        )
        return ResponseEntity.ok(feed)
    }

    @PutMapping("/{checkInId}")
    @Operation(
        summary = "체크인 수정",
        description = "기존 체크인 정보를 수정합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "체크인 수정 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "403", description = "권한 없음"),
            ApiResponse(responseCode = "404", description = "체크인을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun updateCheckIn(
        @Parameter(description = "체크인 ID", required = true)
        @PathVariable checkInId: String,
        @RequestBody request: CheckInUpdateRequest
    ): ResponseEntity<CheckInResponse> {
        // 실제 구현에서는 체크인 수정 로직 수행
        val checkIn = CheckInResponse(
            id = checkInId,
            userId = "current-user-id",
            whiskeyId = "whiskey_456",
            location = request.location ?: "강남 위스키바",
            locationLat = request.locationLat ?: 37.5665,
            locationLng = request.locationLng ?: 126.9780,
            locationType = request.locationType ?: "WHISKEY_BAR",
            rating = request.rating ?: 4.5,
            notes = request.notes ?: "정말 부드럽고 복잡한 맛이에요!",
            images = request.images ?: emptyList(),
            tags = request.tags ?: listOf("싱글몰트", "스모키"),
            likesCount = 24,
            commentsCount = 8,
            sharesCount = 3,
            isLikedByMe = false,
            isPublic = request.isPublic ?: true,
            isFeatured = false,
            createdAt = Instant.now(),
            user = UserInfo(
                id = "current-user-id",
                username = "whiskeylover",
                displayName = "위스키러버",
                profileImageUrl = null,
                isVerified = false
            ),
            whiskey = WhiskeyInfo(
                id = "whiskey_456",
                name = "Macallan 18",
                distillery = "The Macallan",
                imageUrl = null
            )
        )
        return ResponseEntity.ok(checkIn)
    }

    @DeleteMapping("/{checkInId}")
    @Operation(
        summary = "체크인 삭제",
        description = "체크인을 삭제합니다."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "체크인 삭제 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "403", description = "권한 없음"),
            ApiResponse(responseCode = "404", description = "체크인을 찾을 수 없음"),
            ApiResponse(responseCode = "500", description = "서버 내부 오류")
        ]
    )
    fun deleteCheckIn(
        @Parameter(description = "체크인 ID", required = true)
        @PathVariable checkInId: String
    ): ResponseEntity<Void> {
        // 실제 구현에서는 체크인 삭제 로직 수행
        return ResponseEntity.noContent().build()
    }
}
