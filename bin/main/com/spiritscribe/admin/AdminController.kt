package com.spiritscribe.admin

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

/**
 * 어드민 대시보드 컨트롤러
 * 관리자가 시스템을 모니터링하고 가짜 데이터를 생성할 수 있는 웹 인터페이스를 제공한다.
 */
@Controller
@RequestMapping("/admin")
class AdminController {

    /**
     * 어드민 대시보드 메인 페이지
     * 시스템 상태, 통계, 빠른 액션 버튼들을 제공한다.
     */
    @GetMapping("/dashboard")
    fun dashboard(model: Model): String {
        val stats = mapOf(
            "userCount" to 0,
            "activeUsers" to 0,
            "totalCheckIns" to 0,
            "totalWhiskies" to 0,
            "systemStatus" to "정상"
        )
        model.addAttribute("stats", stats)
        model.addAttribute("title", "SpiritScribe Admin Dashboard")
        return "admin/dashboard"
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
     * 어드민 로그인 페이지
     * 어드민 인증을 위한 로그인 폼을 제공한다.
     */
    @GetMapping("/login")
    fun login(model: Model): String {
        model.addAttribute("title", "관리자 로그인")
        return "admin/login"
    }
}
