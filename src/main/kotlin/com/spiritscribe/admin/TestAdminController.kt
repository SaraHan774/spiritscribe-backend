package com.spiritscribe.admin

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 테스트 어드민 컨트롤러
 * 어드민 기능이 제대로 작동하는지 테스트한다.
 */
@RestController
@RequestMapping("/v1/admin")
class TestAdminController {

    @GetMapping("/test")
    fun test(): Map<String, String> {
        return mapOf("message" to "어드민 컨트롤러가 정상적으로 작동합니다!")
    }
}
