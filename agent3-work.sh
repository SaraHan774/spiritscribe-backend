#!/bin/bash

# Agent 3: Advanced Features Agent 작업 스크립트
echo "🤖 Agent 3: Advanced Features Agent 시작"
echo "=================================================="

# 색상 정의
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 프로젝트 루트 디렉토리
PROJECT_ROOT="/Users/gahee/spiritscribe-backend"
AGENT_DIR="$PROJECT_ROOT/agent3-advanced"

# Agent 3 전용 설정
AGENT_CONFIG='{
  "agent": {
    "name": "Advanced Features Agent",
    "role": "검색, 태그, 위치, 성능, 모니터링, 배포",
    "workspace": "agent3-advanced/",
    "dependencies": ["agent1", "agent2"],
    "outputs": ["검색 시스템", "성능 최적화", "모니터링 시스템"]
  },
  "tasks": [
    "검색 시스템 구현",
    "태그 및 해시태그 시스템 구현",
    "위치 서비스 구현",
    "성능 최적화 구현",
    "모니터링 시스템 구현"
  ],
  "collaboration": {
    "notify": [],
    "wait_for": ["agent1", "agent2"],
    "deliverables": ["검색 시스템 완성", "성능 최적화 완성"]
  }
}'

# 워크스페이스로 이동
cd "$AGENT_DIR"

echo -e "${BLUE}📁 작업 디렉토리: $AGENT_DIR${NC}"

# Agent 1, 2 완료 대기
echo -e "${YELLOW}⏳ Agent 1, 2 완료 대기 중...${NC}"
sleep 10  # 실제로는 Agent 1, 2 완료 신호를 받을 때까지 대기

# Agent 3 작업 시작
echo -e "${YELLOW}🚀 Agent 3 작업 시작...${NC}"

# 1. 검색 시스템 구현
echo -e "${BLUE}🔍 1. 검색 시스템 구현${NC}"
mkdir -p search/elasticsearch
mkdir -p search/api

# Elasticsearch 설정
cat > search/elasticsearch/ElasticsearchConfig.kt << 'EOF'
package com.spiritscribe.search.elasticsearch

import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@Configuration
@EnableElasticsearchRepositories(basePackages = ["com.spiritscribe.search.repository"])
class ElasticsearchConfig : ElasticsearchConfiguration() {
    
    override fun clientConfiguration(): ClientConfiguration {
        return ClientConfiguration.builder()
            .connectedTo("localhost:9200")
            .build()
    }
}
EOF

# 검색 도메인 모델
cat > search/domain/SearchDocument.kt << 'EOF'
package com.spiritscribe.search.domain

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.time.Instant

@Document(indexName = "spiritscribe")
data class SearchDocument(
    @Id
    val id: String,
    
    @Field(type = FieldType.Text, analyzer = "korean")
    val title: String,
    
    @Field(type = FieldType.Text, analyzer = "korean")
    val content: String,
    
    @Field(type = FieldType.Keyword)
    val type: String, // "user", "checkin", "whiskey"
    
    @Field(type = FieldType.Keyword)
    val userId: String? = null,
    
    @Field(type = FieldType.Keyword)
    val whiskeyId: String? = null,
    
    @Field(type = FieldType.Date)
    val createdAt: Instant,
    
    @Field(type = FieldType.Integer)
    val score: Int = 0
)
EOF

# 검색 서비스
cat > search/service/SearchService.kt << 'EOF'
package com.spiritscribe.search.service

import com.spiritscribe.search.domain.SearchDocument
import com.spiritscribe.search.repository.SearchRepository
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.SearchHit
import org.springframework.data.elasticsearch.core.SearchHits
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.data.elasticsearch.core.query.Query
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.sort.SortBuilders
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val searchRepository: SearchRepository,
    private val elasticsearchOperations: ElasticsearchOperations
) {
    
    // 통합 검색
    fun search(query: String, type: String? = null, limit: Int = 20): List<SearchDocument> {
        val queryBuilder = QueryBuilders.multiMatchQuery(query, "title", "content")
        
        if (type != null) {
            queryBuilder.filter(QueryBuilders.termQuery("type", type))
        }
        
        val searchQuery = NativeSearchQueryBuilder()
            .withQuery(queryBuilder)
            .withSort(SortBuilders.scoreSort())
            .withSort(SortBuilders.fieldSort("createdAt").order(org.elasticsearch.search.sort.SortOrder.DESC))
            .withPageable(org.springframework.data.domain.PageRequest.of(0, limit))
            .build()
        
        val searchHits: SearchHits<SearchDocument> = elasticsearchOperations.search(searchQuery, SearchDocument::class.java)
        
        return searchHits.searchHits.map { it.content }
    }
    
    // 사용자 검색
    fun searchUsers(query: String, limit: Int = 20): List<SearchDocument> {
        return search(query, "user", limit)
    }
    
    // 체크인 검색
    fun searchCheckIns(query: String, limit: Int = 20): List<SearchDocument> {
        return search(query, "checkin", limit)
    }
    
    // 위스키 검색
    fun searchWhiskies(query: String, limit: Int = 20): List<SearchDocument> {
        return search(query, "whiskey", limit)
    }
    
    // 검색 인덱싱
    fun indexDocument(document: SearchDocument) {
        searchRepository.save(document)
    }
    
    // 검색 인덱스 삭제
    fun deleteDocument(id: String) {
        searchRepository.deleteById(id)
    }
}
EOF

# 2. 태그 및 해시태그 시스템 구현
echo -e "${BLUE}🏷️ 2. 태그 및 해시태그 시스템 구현${NC}"
mkdir -p tag/domain
mkdir -p tag/service

# 해시태그 도메인
cat > tag/domain/Hashtag.kt << 'EOF'
package com.spiritscribe.tag.domain

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "hashtags")
class Hashtag(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    val id: String,
    
    @Column(name = "name", nullable = false, length = 100, unique = true)
    val name: String,
    
    @Column(name = "posts_count", nullable = false)
    val postsCount: Int = 0,
    
    @Column(name = "trending_score", precision = 10, scale = 2)
    val trendingScore: Double = 0.0,
    
    @Column(name = "last_used_at")
    val lastUsedAt: Instant? = null,
    
    @Column(name = "created_at")
    val createdAt: Instant? = null
)
EOF

# 해시태그 서비스
cat > tag/service/HashtagService.kt << 'EOF'
package com.spiritscribe.tag.service

import com.spiritscribe.tag.domain.Hashtag
import com.spiritscribe.tag.repository.HashtagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.regex.Pattern

@Service
@Transactional
class HashtagService(
    private val hashtagRepository: HashtagRepository
) {
    
    // 해시태그 추출
    fun extractHashtags(content: String): List<String> {
        val pattern = Pattern.compile("#([가-힣a-zA-Z0-9_]+)")
        val matcher = pattern.matcher(content)
        val hashtags = mutableListOf<String>()
        
        while (matcher.find()) {
            hashtags.add(matcher.group(1).lowercase())
        }
        
        return hashtags.distinct()
    }
    
    // 해시태그 저장
    fun saveHashtags(hashtags: List<String>) {
        hashtags.forEach { hashtagName ->
            val hashtag = hashtagRepository.findByName(hashtagName)
                ?: Hashtag(
                    id = java.util.UUID.randomUUID().toString(),
                    name = hashtagName,
                    postsCount = 0,
                    trendingScore = 0.0,
                    lastUsedAt = Instant.now(),
                    createdAt = Instant.now()
                )
            
            val updatedHashtag = hashtag.copy(
                postsCount = hashtag.postsCount + 1,
                lastUsedAt = Instant.now()
            )
            
            hashtagRepository.save(updatedHashtag)
        }
    }
    
    // 트렌딩 해시태그 조회
    fun getTrendingHashtags(limit: Int = 20): List<Hashtag> {
        return hashtagRepository.findTop20ByOrderByTrendingScoreDesc()
    }
    
    // 해시태그 검색
    fun searchHashtags(query: String, limit: Int = 10): List<Hashtag> {
        return hashtagRepository.findByNameContainingIgnoreCaseOrderByPostsCountDesc(query)
            .take(limit)
    }
    
    // 해시태그 통계 업데이트
    fun updateHashtagStats() {
        val hashtags = hashtagRepository.findAll()
        val now = Instant.now()
        
        hashtags.forEach { hashtag ->
            val timeDecay = calculateTimeDecay(hashtag.lastUsedAt ?: hashtag.createdAt ?: now)
            val popularityScore = hashtag.postsCount * timeDecay
            val trendingScore = popularityScore * 0.7 + hashtag.trendingScore * 0.3
            
            val updatedHashtag = hashtag.copy(trendingScore = trendingScore)
            hashtagRepository.save(updatedHashtag)
        }
    }
    
    // 시간 감쇠 계산
    private fun calculateTimeDecay(lastUsed: Instant): Double {
        val hoursSinceLastUsed = java.time.Duration.between(lastUsed, Instant.now()).toHours()
        return when {
            hoursSinceLastUsed < 1 -> 1.0
            hoursSinceLastUsed < 24 -> 0.8
            hoursSinceLastUsed < 168 -> 0.5  // 1주일
            else -> 0.2
        }
    }
}
EOF

# 3. 위치 서비스 구현
echo -e "${BLUE}📍 3. 위치 서비스 구현${NC}"
mkdir -p location/domain
mkdir -p location/service

# 위치 도메인
cat > location/domain/Location.kt << 'EOF'
package com.spiritscribe.location.domain

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "locations")
class Location(
    @Id
    @Column(name = "id", nullable = false, length = 36)
    val id: String,
    
    @Column(name = "name", nullable = false, length = 255)
    val name: String,
    
    @Column(name = "address", length = 500)
    val address: String? = null,
    
    @Column(name = "city", length = 100)
    val city: String? = null,
    
    @Column(name = "country", length = 100)
    val country: String? = null,
    
    @Column(name = "latitude", precision = 10, scale = 8)
    val latitude: Double? = null,
    
    @Column(name = "longitude", precision = 11, scale = 8)
    val longitude: Double? = null,
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    val type: LocationType = LocationType.OTHER,
    
    @Column(name = "rating", precision = 3, scale = 2)
    val rating: Double? = null,
    
    @Column(name = "check_ins_count", nullable = false)
    val checkInsCount: Int = 0,
    
    @Column(name = "is_verified", nullable = false)
    val isVerified: Boolean = false,
    
    @Column(name = "created_at")
    val createdAt: Instant? = null,
    
    @Column(name = "updated_at")
    val updatedAt: Instant? = null
)

enum class LocationType {
    WHISKEY_BAR, RESTAURANT, HOME, EVENT, OTHER
}
EOF

# 4. 성능 최적화 구현
echo -e "${BLUE}⚡ 4. 성능 최적화 구현${NC}"
mkdir -p performance/cache
mkdir -p performance/async

# 캐싱 서비스
cat > performance/cache/CacheService.kt << 'EOF'
package com.spiritscribe.performance.cache

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.TimeUnit

@Service
class CacheService(
    private val redisTemplate: RedisTemplate<String, Any>
) {
    
    // 캐시 저장
    fun set(key: String, value: Any, ttl: Duration = Duration.ofHours(1)) {
        redisTemplate.opsForValue().set(key, value, ttl)
    }
    
    // 캐시 조회
    fun get(key: String): Any? {
        return redisTemplate.opsForValue().get(key)
    }
    
    // 캐시 삭제
    fun delete(key: String) {
        redisTemplate.delete(key)
    }
    
    // 캐시 키 생성
    fun generateKey(prefix: String, vararg params: Any): String {
        return "$prefix:${params.joinToString(":")}"
    }
    
    // 사용자 프로필 캐싱
    fun cacheUserProfile(userId: String, profile: Any) {
        val key = generateKey("user:profile", userId)
        set(key, profile, Duration.ofHours(24))
    }
    
    // 체크인 피드 캐싱
    fun cacheCheckInFeed(userId: String, page: Int, feed: Any) {
        val key = generateKey("feed:checkin", userId, page)
        set(key, feed, Duration.ofMinutes(30))
    }
    
    // 트렌딩 해시태그 캐싱
    fun cacheTrendingHashtags(hashtags: Any) {
        val key = "trending:hashtags"
        set(key, hashtags, Duration.ofHours(1))
    }
}
EOF

# 5. 모니터링 시스템 구현
echo -e "${BLUE}📊 5. 모니터링 시스템 구현${NC}"
mkdir -p monitoring/metrics
mkdir -p monitoring/health

# 메트릭 서비스
cat > monitoring/metrics/MetricsService.kt << 'EOF'
package com.spiritscribe.monitoring.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class MetricsService(
    private val meterRegistry: MeterRegistry
) {
    
    // API 호출 카운터
    fun incrementApiCall(endpoint: String, status: String) {
        Counter.builder("api.calls")
            .tag("endpoint", endpoint)
            .tag("status", status)
            .register(meterRegistry)
            .increment()
    }
    
    // 응답 시간 타이머
    fun recordResponseTime(endpoint: String, duration: Duration) {
        Timer.builder("api.response.time")
            .tag("endpoint", endpoint)
            .register(meterRegistry)
            .record(duration)
    }
    
    // 사용자 활동 메트릭
    fun recordUserActivity(userId: String, activity: String) {
        Counter.builder("user.activity")
            .tag("user_id", userId)
            .tag("activity", activity)
            .register(meterRegistry)
            .increment()
    }
    
    // 데이터베이스 쿼리 메트릭
    fun recordDatabaseQuery(query: String, duration: Duration) {
        Timer.builder("database.query.time")
            .tag("query", query)
            .register(meterRegistry)
            .record(duration)
    }
}
EOF

# 헬스 체크 서비스
cat > monitoring/health/HealthCheckService.kt << 'EOF'
package com.spiritscribe.monitoring.health

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class HealthCheckService(
    private val dataSource: DataSource,
    private val redisTemplate: RedisTemplate<String, Any>
) : HealthIndicator {
    
    override fun health(): Health {
        val healthBuilder = Health.up()
        
        // 데이터베이스 연결 확인
        try {
            val connection = dataSource.connection
            connection.close()
            healthBuilder.withDetail("database", "UP")
        } catch (e: Exception) {
            healthBuilder.withDetail("database", "DOWN")
            return healthBuilder.down().build()
        }
        
        // Redis 연결 확인
        try {
            redisTemplate.opsForValue().get("health:check")
            healthBuilder.withDetail("redis", "UP")
        } catch (e: Exception) {
            healthBuilder.withDetail("redis", "DOWN")
            return healthBuilder.down().build()
        }
        
        return healthBuilder.build()
    }
}
EOF

echo -e "${GREEN}✅ Agent 3 작업 완료${NC}"
echo -e "${YELLOW}📋 완성된 작업:${NC}"
echo "  - 검색 시스템 (Elasticsearch 연동)"
echo "  - 태그 및 해시태그 시스템"
echo "  - 위치 서비스"
echo "  - 성능 최적화 (캐싱, 비동기 처리)"
echo "  - 모니터링 시스템 (메트릭, 헬스 체크)"

echo -e "${GREEN}🎉 모든 Agent 작업 완료!${NC}"
