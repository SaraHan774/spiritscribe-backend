package com.spiritscribe.admin.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * 어드민 인증 컨트롤러
 * 어드민 로그인, 로그아웃 기능을 제공한다.
 */
@Controller
@RequestMapping("/admin")
class AdminAuthController {

    /**
     * 어드민 로그인 페이지
     * 어드민 인증을 위한 로그인 폼을 제공한다.
     */
    @GetMapping("/login")
    fun login(
        @RequestParam(value = "error", required = false) error: String?,
        @RequestParam(value = "logout", required = false) logout: String?,
        model: Model
    ): String {
        if (error != null) {
            model.addAttribute("errorMessage", "잘못된 사용자 이름 또는 비밀번호입니다.")
        }
        if (logout != null) {
            model.addAttribute("successMessage", "성공적으로 로그아웃되었습니다.")
        }
        return "admin/login"
    }

    /**
     * 어드민 로그아웃
     * 현재 세션을 종료하고 로그인 페이지로 리다이렉트한다.
     */
    @GetMapping("/logout")
    fun logout(): String {
        return "redirect:/admin/login?logout=true"
    }
}
