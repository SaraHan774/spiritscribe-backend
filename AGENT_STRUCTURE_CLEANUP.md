# Agent 구조 정리 및 중복 제거 계획

## 🎯 현재 문제점

### **1. 중복된 파일들**
- `src/main/kotlin/com/spiritscribe/auth/` vs `agent1-core/auth/`
- `src/main/kotlin/com/spiritscribe/user/` vs `agent1-core/user/`
- `src/main/kotlin/com/spiritscribe/checkin/` vs `agent2-social/checkin/`
- `src/main/kotlin/com/spiritscribe/social/` vs `agent2-social/social/`

### **2. 혼란스러운 구조**
- 실제 작동하는 코드: `src/main/kotlin/`
- Agent별 작업 공간: `agent1-core/`, `agent2-social/`, `agent3-advanced/`
- 빌드된 코드: `bin/main/`

## 🏗️ 새로운 구조 계획

### **Agent 1: Core Infrastructure (agent1-core/)**
```
agent1-core/
├── auth/
│   ├── api/AuthController.kt
│   ├── service/JwtTokenService.kt
│   └── jwt/JwtTokenProvider.kt
├── user/
│   ├── api/UserController.kt
│   ├── domain/User.kt
│   ├── repository/UserRepository.kt
│   └── service/UserService.kt
├── database/
│   └── migrations/
│       ├── V1__baseline.sql
│       └── V2__add_user_tables.sql
└── config/
    ├── SecurityConfig.kt
    └── CorsConfig.kt
```

### **Agent 2: Social Features (agent2-social/)**
```
agent2-social/
├── checkin/
│   ├── api/CheckInController.kt
│   └── domain/CheckIn.kt
├── social/
│   ├── api/SocialController.kt
│   └── like/Like.kt
├── notification/
│   └── domain/Notification.kt
└── image/
    └── service/ImageService.kt
```

### **Agent 3: Advanced Features (agent3-advanced/)**
```
agent3-advanced/
├── search/
│   ├── api/SearchController.kt
│   └── domain/SearchDocument.kt
├── performance/
│   ├── cache/CacheService.kt
│   └── optimization/OptimizationService.kt
├── monitoring/
│   ├── metrics/MetricsService.kt
│   └── health/HealthController.kt
└── deployment/
    └── config/DeploymentConfig.kt
```

### **Shared Resources (shared/)**
```
shared/
├── config/
│   ├── OpenApiConfig.kt
│   └── WebSocketConfig.kt
├── common/
│   └── utils/CommonUtils.kt
└── docs/
    ├── API_DOCUMENTATION.md
    └── DEPLOYMENT_GUIDE.md
```

## 🔄 마이그레이션 계획

### **Phase 1: Agent 1 (Core Infrastructure)**
1. `src/main/kotlin/com/spiritscribe/auth/` → `agent1-core/auth/`
2. `src/main/kotlin/com/spiritscribe/user/` → `agent1-core/user/`
3. `src/main/kotlin/com/spiritscribe/config/SecurityConfig.kt` → `agent1-core/config/`
4. `src/main/kotlin/com/spiritscribe/config/CorsConfig.kt` → `agent1-core/config/`

### **Phase 2: Agent 2 (Social Features)**
1. `src/main/kotlin/com/spiritscribe/checkin/` → `agent2-social/checkin/`
2. `src/main/kotlin/com/spiritscribe/social/` → `agent2-social/social/`
3. 새로운 notification, image 기능 추가

### **Phase 3: Agent 3 (Advanced Features)**
1. `src/main/kotlin/com/spiritscribe/health/` → `agent3-advanced/monitoring/`
2. 새로운 search, performance, monitoring 기능 추가

### **Phase 4: Shared Resources**
1. `src/main/kotlin/com/spiritscribe/config/OpenApiConfig.kt` → `shared/config/`
2. `src/main/kotlin/com/spiritscribe/config/WebSocketConfig.kt` → `shared/config/`
3. 공통 유틸리티 및 문서 정리

## 📋 Agent별 담당 영역

### **Agent 1: Core Infrastructure**
- ✅ **인증 시스템**: JWT, 회원가입, 로그인
- ✅ **사용자 관리**: 프로필, 통계, 팔로우
- ✅ **데이터베이스**: 스키마, 마이그레이션
- ✅ **보안**: Spring Security, CORS
- ✅ **기본 설정**: 애플리케이션 설정

### **Agent 2: Social Features**
- ✅ **체크인 시스템**: CRUD, 피드, 위치
- ✅ **소셜 인터랙션**: 좋아요, 댓글, 공유
- ✅ **알림 시스템**: WebSocket, FCM
- ✅ **이미지 관리**: 업로드, 리사이징, 스토리지

### **Agent 3: Advanced Features**
- ✅ **검색 시스템**: Elasticsearch, 태그
- ✅ **성능 최적화**: 캐싱, 비동기 처리
- ✅ **모니터링**: 메트릭, 헬스 체크
- ✅ **배포**: Docker, CI/CD

## 🚀 실행 계획

### **1단계: Agent 1 정리**
- 인증 관련 파일들을 `agent1-core/`로 이동
- 사용자 관련 파일들을 `agent1-core/`로 이동
- 보안 설정을 `agent1-core/`로 이동

### **2단계: Agent 2 정리**
- 체크인 관련 파일들을 `agent2-social/`로 이동
- 소셜 관련 파일들을 `agent2-social/`로 이동
- 알림 시스템 구현

### **3단계: Agent 3 정리**
- 헬스 체크를 `agent3-advanced/`로 이동
- 검색, 성능, 모니터링 기능 구현

### **4단계: Shared 정리**
- 공통 설정을 `shared/`로 이동
- 문서 정리 및 통합

## 📊 예상 결과

### **정리 후 구조**
```
spiritscribe-backend/
├── agent1-core/          # Core Infrastructure
├── agent2-social/        # Social Features  
├── agent3-advanced/      # Advanced Features
├── shared/              # Shared Resources
├── src/main/kotlin/     # 통합된 메인 코드
└── docs/               # 통합 문서
```

### **장점**
- ✅ **명확한 역할 분담**
- ✅ **중복 제거**
- ✅ **유지보수성 향상**
- ✅ **협업 효율성 증대**

## 🎯 다음 단계

1. **Agent 1 정리 시작**
2. **중복 파일 제거**
3. **통합 테스트**
4. **문서 업데이트**
