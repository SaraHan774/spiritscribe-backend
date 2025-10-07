package com.spiritscribe.admin

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

/**
 * 어드민 웹 컨트롤러
 * 관리자 전용 웹 페이지를 제공한다.
 */
@Controller
@RequestMapping("/admin")
class AdminWebController {

    /**
     * 어드민 로그인 페이지
     */
    @GetMapping("/login")
    fun login(model: Model): String {
        model.addAttribute("title", "관리자 로그인")
        return "admin/login"
    }

    /**
     * 어드민 대시보드
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
     */
    @GetMapping("/fake-data")
    fun fakeDataPage(model: Model): String {
        model.addAttribute("title", "가짜 데이터 생성")
        return "admin/fake-data"
    }
}
