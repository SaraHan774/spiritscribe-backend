package com.spiritscribe.admin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

/**
 * 어드민 보안 설정
 * 어드민 페이지에 대한 접근 제어를 설정한다.
 */
@Configuration
@EnableWebSecurity
class AdminSecurityConfig {

    /**
     * 어드민 전용 보안 필터 체인
     * 어드민 페이지는 별도의 인증이 필요하다.
     */
    @Bean
    fun adminSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .securityMatcher("/admin/**")
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/admin/css/**", "/admin/js/**", "/admin/images/**").permitAll()
                    .requestMatchers("/admin/login").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .formLogin { form ->
                form
                    .loginPage("/admin/login")
                    .defaultSuccessUrl("/admin/dashboard", true)
                    .failureUrl("/admin/login?error=true")
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutUrl("/admin/logout")
                    .logoutSuccessUrl("/admin/login?logout=true")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            }
            .sessionManagement { session ->
                session
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
            }
            .build()
    }

    /**
     * 어드민 사용자 상세 서비스
     * 메모리에 어드민 사용자 정보를 저장한다.
     */
    @Bean
    fun adminUserDetailsService(): UserDetailsService {
        val admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("ADMIN")
            .build()
        
        val testAdmin = User.builder()
            .username("testadmin")
            .password(passwordEncoder().encode("test123"))
            .roles("ADMIN")
            .build()
        
        return InMemoryUserDetailsManager(admin, testAdmin)
    }

    /**
     * 비밀번호 인코더
     * BCrypt를 사용하여 비밀번호를 암호화한다.
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
