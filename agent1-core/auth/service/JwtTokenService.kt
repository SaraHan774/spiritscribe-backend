package com.spiritscribe.auth.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtTokenService(
    @Value("\${security.jwt.secret}") private val secret: String,
    @Value("\${security.jwt.access-token-ttl-seconds}") private val accessTokenTtl: Long
) {
    
    fun generateToken(userId: String, username: String): String {
        // 간단한 JWT 토큰 생성 (실제 구현에서는 JWT 라이브러리 사용)
        val header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}"
        val payload = "{\"sub\":\"$userId\",\"username\":\"$username\",\"iat\":${System.currentTimeMillis() / 1000},\"exp\":${(System.currentTimeMillis() / 1000) + accessTokenTtl}}"
        
        val encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(header.toByteArray())
        val encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.toByteArray())
        
        val signature = generateSignature("$encodedHeader.$encodedPayload")
        
        return "$encodedHeader.$encodedPayload.$signature"
    }
    
    fun validateToken(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return false
            
            val header = parts[0]
            val payload = parts[1]
            val signature = parts[2]
            
            val expectedSignature = generateSignature("$header.$payload")
            signature == expectedSignature
        } catch (e: Exception) {
            false
        }
    }
    
    fun getUserIdFromToken(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null
            
            val payload = String(Base64.getUrlDecoder().decode(parts[1]))
            val json = payload.substringAfter("\"sub\":\"").substringBefore("\"")
            json
        } catch (e: Exception) {
            null
        }
    }
    
    fun getUsernameFromToken(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null
            
            val payload = String(Base64.getUrlDecoder().decode(parts[1]))
            val json = payload.substringAfter("\"username\":\"").substringBefore("\"")
            json
        } catch (e: Exception) {
            null
        }
    }
    
    private fun generateSignature(data: String): String {
        // 간단한 HMAC-SHA256 구현 (실제 구현에서는 보안 라이브러리 사용)
        val hmac = java.security.MessageDigest.getInstance("SHA-256")
        val hash = hmac.digest((data + secret).toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash)
    }
}
