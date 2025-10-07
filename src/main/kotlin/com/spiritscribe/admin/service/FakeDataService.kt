package com.spiritscribe.admin.service

import com.spiritscribe.user.domain.User
import com.spiritscribe.user.repository.UserRepository
import net.datafaker.Faker
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

/**
 * 가짜 데이터 생성 서비스
 * 테스트 및 개발을 위한 가짜 데이터를 생성한다.
 */
@Service
class FakeDataService(
    private val userRepository: UserRepository
) {
    private val faker = Faker(Locale.KOREAN)

    /**
     * 가짜 사용자 생성
     * 지정된 수만큼 가짜 사용자를 생성한다.
     */
    fun generateFakeUsers(count: Int): Int {
        val users = mutableListOf<User>()
        
        repeat(count) {
            val user = User(
                id = UUID.randomUUID().toString(),
                username = faker.name().username().lowercase().replace(" ", ""),
                displayName = faker.name().fullName(),
                email = faker.internet().emailAddress(),
                createdAt = Instant.now()
            )
            users.add(user)
        }
        
        userRepository.saveAll(users)
        return count
    }

    /**
     * 가짜 체크인 생성
     * 지정된 수만큼 가짜 체크인을 생성한다.
     */
    fun generateFakeCheckIns(count: Int): Int {
        // 실제로는 체크인 저장소에 저장
        // 여기서는 더미 구현
        return count
    }

    /**
     * 가짜 위스키 생성
     * 지정된 수만큼 가짜 위스키를 생성한다.
     */
    fun generateFakeWhiskies(count: Int): Int {
        // 실제로는 위스키 저장소에 저장
        // 여기서는 더미 구현
        return count
    }

    /**
     * 가짜 알림 생성
     * 지정된 수만큼 가짜 알림을 생성한다.
     */
    fun generateFakeNotifications(count: Int): Int {
        // 실제로는 알림 저장소에 저장
        // 여기서는 더미 구현
        return count
    }

    /**
     * 가짜 팔로우 관계 생성
     * 사용자 간의 가짜 팔로우 관계를 생성한다.
     */
    fun generateFakeFollows(count: Int): Int {
        // 실제로는 팔로우 저장소에 저장
        // 여기서는 더미 구현
        return count
    }

    /**
     * 가짜 댓글 생성
     * 체크인에 가짜 댓글을 생성한다.
     */
    fun generateFakeComments(count: Int): Int {
        // 실제로는 댓글 저장소에 저장
        // 여기서는 더미 구현
        return count
    }

    /**
     * 가짜 좋아요 생성
     * 체크인에 가짜 좋아요를 생성한다.
     */
    fun generateFakeLikes(count: Int): Int {
        // 실제로는 좋아요 저장소에 저장
        // 여기서는 더미 구현
        return count
    }

    /**
     * 가짜 해시태그 생성
     * 인기 있는 해시태그들을 생성한다.
     */
    fun generateFakeHashtags(count: Int): Int {
        val hashtags = listOf(
            "#싱글몰트", "#스모키", "#프리미엄", "#위스키", "#맥알란",
            "#글렌피딕", "#발렌타인", "#조니워커", "#시바스리갈", "#잭다니엘스"
        )
        
        // 실제로는 해시태그 저장소에 저장
        // 여기서는 더미 구현
        return minOf(count, hashtags.size)
    }

    /**
     * 가짜 위치 데이터 생성
     * 위스키바, 레스토랑 등의 가짜 위치를 생성한다.
     */
    fun generateFakeLocations(count: Int): Int {
        // 실제로는 위치 저장소에 저장
        // 여기서는 더미 구현
        return count
    }

    /**
     * 모든 가짜 데이터 생성
     * 한 번에 모든 유형의 가짜 데이터를 생성한다.
     */
    fun generateAllFakeData(userCount: Int, checkInCount: Int, whiskeyCount: Int): Map<String, Int> {
        val results = mutableMapOf<String, Int>()
        
        results["users"] = generateFakeUsers(userCount)
        results["checkins"] = generateFakeCheckIns(checkInCount)
        results["whiskies"] = generateFakeWhiskies(whiskeyCount)
        results["notifications"] = generateFakeNotifications(50)
        results["follows"] = generateFakeFollows(100)
        results["comments"] = generateFakeComments(200)
        results["likes"] = generateFakeLikes(500)
        results["hashtags"] = generateFakeHashtags(20)
        results["locations"] = generateFakeLocations(30)
        
        return results
    }
}
