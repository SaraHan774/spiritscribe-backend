package com.spiritscribe.auth.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(
    @Value("${security.jwt.secret}") private val secret: String,
    @Value("${security.jwt.access-token-ttl-seconds}") private val accessTokenTtl: Long
) {
    private val key: Key = Keys.hmacShaKeyFor(secret.toByteArray())
    
    fun generateToken(userId: String, username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + accessTokenTtl * 1000)
        
        return Jwts.builder()
            .setSubject(userId)
            .claim("username", username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }
    
    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: JwtException) {
            false
        }
    }
}
