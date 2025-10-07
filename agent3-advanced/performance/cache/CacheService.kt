package com.spiritscribe.performance.cache

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class CacheService(
    private val redisTemplate: RedisTemplate<String, Any>
) {
    
    fun set(key: String, value: Any, ttl: Duration = Duration.ofHours(1)) {
        redisTemplate.opsForValue().set(key, value, ttl)
    }
    
    fun get(key: String): Any? {
        return redisTemplate.opsForValue().get(key)
    }
    
    fun delete(key: String) {
        redisTemplate.delete(key)
    }
    
    fun generateKey(prefix: String, vararg params: Any): String {
        return "$prefix:${params.joinToString(":")}"
    }
}
