package com.spiritscribe.admin.controller

import com.spiritscribe.admin.service.AdminService
import com.spiritscribe.admin.service.FakeDataService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

/**
 * 어드민 대시보드 컨트롤러
 * 관리자가 시스템을 모니터링하고 가짜 데이터를 생성할 수 있는 웹 인터페이스를 제공한다.
 */
@Controller
@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService,
    private val fakeDataService: FakeDataService
) {

    /**
     * 어드민 대시보드 메인 페이지
     * 시스템 상태, 통계, 빠른 액션 버튼들을 제공한다.
     */
    @GetMapping("/dashboard")
    fun dashboard(model: Model): String {
        val stats = adminService.getSystemStats()
        model.addAttribute("stats", stats)
        model.addAttribute("title", "SpiritScribe Admin Dashboard")
        return "admin/dashboard"
    }

    /**
     * 사용자 관리 페이지
     * 사용자 목록, 검색, 통계를 제공한다.
     */
    @GetMapping("/users")
    fun users(model: Model): String {
        val users = adminService.getAllUsers()
        model.addAttribute("users", users)
        model.addAttribute("title", "사용자 관리")
        return "admin/users"
    }

    /**
     * 체크인 관리 페이지
     * 체크인 목록, 통계, 필터링을 제공한다.
     */
    @GetMapping("/checkins")
    fun checkins(model: Model): String {
        val checkins = adminService.getAllCheckIns()
        model.addAttribute("checkins", checkins)
        model.addAttribute("title", "체크인 관리")
        return "admin/checkins"
    }

    /**
     * 가짜 데이터 생성 페이지
     * 다양한 유형의 가짜 데이터를 생성할 수 있는 인터페이스를 제공한다.
     */
    @GetMapping("/fake-data")
    fun fakeDataPage(model: Model): String {
        model.addAttribute("title", "가짜 데이터 생성")
        return "admin/fake-data"
    }

    /**
     * 가짜 사용자 생성
     * 지정된 수만큼 가짜 사용자를 생성한다.
     */
    @PostMapping("/fake-data/users")
    @ResponseBody
    fun generateFakeUsers(@RequestParam count: Int): Map<String, Any> {
        val result = fakeDataService.generateFakeUsers(count)
        return mapOf(
            "success" to true,
            "message" to "${result}명의 가짜 사용자가 생성되었습니다.",
            "count" to result
        )
    }

    /**
     * 가짜 체크인 생성
     * 지정된 수만큼 가짜 체크인을 생성한다.
     */
    @PostMapping("/fake-data/checkins")
    @ResponseBody
    fun generateFakeCheckIns(@RequestParam count: Int): Map<String, Any> {
        val result = fakeDataService.generateFakeCheckIns(count)
        return mapOf(
            "success" to true,
            "message" to "${result}개의 가짜 체크인이 생성되었습니다.",
            "count" to result
        )
    }

    /**
     * 가짜 위스키 생성
     * 지정된 수만큼 가짜 위스키를 생성한다.
     */
    @PostMapping("/fake-data/whiskies")
    @ResponseBody
    fun generateFakeWhiskies(@RequestParam count: Int): Map<String, Any> {
        val result = fakeDataService.generateFakeWhiskies(count)
        return mapOf(
            "success" to true,
            "message" to "${result}개의 가짜 위스키가 생성되었습니다.",
            "count" to result
        )
    }

    /**
     * 시스템 통계 조회
     * 실시간 시스템 통계를 반환한다.
     */
    @GetMapping("/api/stats")
    @ResponseBody
    fun getStats(): Map<String, Any> {
        return adminService.getSystemStats()
    }

    /**
     * 데이터베이스 초기화
     * 모든 가짜 데이터를 삭제한다.
     */
    @PostMapping("/reset-data")
    @ResponseBody
    fun resetData(): Map<String, Any> {
        val result = adminService.resetAllData()
        return mapOf(
            "success" to true,
            "message" to "모든 가짜 데이터가 삭제되었습니다.",
            "deletedCount" to result
        )
    }
}
