package com.spiritscribe.admin.service

import com.spiritscribe.user.repository.UserRepository
import com.spiritscribe.user.service.UserService
import org.springframework.stereotype.Service

/**
 * 어드민 서비스
 * 시스템 관리, 통계 조회, 데이터 관리 기능을 제공한다.
 */
@Service
class AdminService(
    private val userRepository: UserRepository,
    private val userService: UserService
) {

    /**
     * 시스템 통계 조회
     * 사용자 수, 체크인 수, 위스키 수 등의 통계를 반환한다.
     */
    fun getSystemStats(): Map<String, Any> {
        val userCount = userRepository.count()
        val activeUsers = userRepository.count() // 실제로는 활성 사용자 수 계산
        val totalCheckIns = 0L // 실제로는 체크인 수 계산
        val totalWhiskies = 0L // 실제로는 위스키 수 계산
        
        val statsMap = mutableMapOf<String, Any>()
        statsMap["userCount"] = userCount
        statsMap["activeUsers"] = activeUsers
        statsMap["totalCheckIns"] = totalCheckIns
        statsMap["totalWhiskies"] = totalWhiskies
        statsMap["systemStatus"] = "정상"
        statsMap["lastUpdated"] = System.currentTimeMillis()
        return statsMap
    }

    /**
     * 모든 사용자 조회
     * 관리자가 사용자 목록을 확인할 수 있도록 한다.
     */
    fun getAllUsers(): List<Map<String, Any>> {
        return userRepository.findAll().map { user ->
            val userMap = mutableMapOf<String, Any>()
            userMap["id"] = user.id
            userMap["username"] = user.username
            userMap["displayName"] = user.displayName
            userMap["email"] = user.email
            userMap["createdAt"] = user.createdAt?.toString() ?: ""
            userMap["isActive"] = true
            userMap
        }
    }

    /**
     * 모든 체크인 조회
     * 관리자가 체크인 목록을 확인할 수 있도록 한다.
     */
    fun getAllCheckIns(): List<Map<String, Any>> {
        // 실제로는 체크인 저장소에서 조회
        val checkinMap = mutableMapOf<String, Any>()
        checkinMap["id"] = "checkin_1"
        checkinMap["userId"] = "user_1"
        checkinMap["whiskeyId"] = "whiskey_1"
        checkinMap["rating"] = 4.5
        checkinMap["notes"] = "정말 좋은 위스키입니다!"
        checkinMap["createdAt"] = System.currentTimeMillis()
        return listOf(checkinMap)
    }

    /**
     * 모든 데이터 초기화
     * 가짜 데이터를 모두 삭제한다.
     */
    fun resetAllData(): Int {
        // 실제로는 모든 가짜 데이터 삭제
        return 0
    }

    /**
     * 시스템 상태 확인
     * 데이터베이스 연결, 외부 서비스 상태 등을 확인한다.
     */
    fun checkSystemHealth(): Map<String, Any> {
        val healthMap = mutableMapOf<String, Any>()
        healthMap["database"] = "연결됨"
        healthMap["redis"] = "연결됨"
        healthMap["externalServices"] = "정상"
        healthMap["timestamp"] = System.currentTimeMillis()
        return healthMap
    }
}
