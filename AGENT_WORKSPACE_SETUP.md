# SpiritScribe Backend - 3 Agent 병렬 개발 설정

## 🤖 Agent 구성

### Agent 1: Core Infrastructure Agent
**담당 영역**: 데이터베이스, 인증, 사용자 관리
**워크스페이스**: `agent1-core/`

### Agent 2: Social Features Agent  
**담당 영역**: 체크인, 소셜 인터랙션, 알림
**워크스페이스**: `agent2-social/`

### Agent 3: Advanced Features Agent
**담당 영역**: 검색, 분석, 성능, 배포
**워크스페이스**: `agent3-advanced/`

---

## 📋 Agent별 상세 TODO

### 🔧 Agent 1: Core Infrastructure Agent

#### Phase 1: 데이터베이스 스키마 (1주)
- [ ] **사용자 관련 테이블**
  - [ ] `users` 테이블 최적화
  - [ ] `user_stats` 테이블 완성
  - [ ] `follows` 테이블 (팔로우 관계)
  - [ ] `blocks` 테이블 (차단 관계)

- [ ] **Flyway 마이그레이션**
  - [ ] V2__add_user_tables.sql
  - [ ] V3__add_user_indexes.sql
  - [ ] V4__add_user_triggers.sql

#### Phase 2: JWT 인증 시스템 (1주)
- [ ] **JWT 토큰 관리**
  - [ ] JwtTokenProvider 구현
  - [ ] JwtAuthenticationFilter 구현
  - [ ] JwtAuthorizationFilter 구현

- [ ] **인증 API**
  - [ ] 회원가입 API (`/v1/auth/register`)
  - [ ] 로그인 API (`/v1/auth/login`)
  - [ ] 토큰 갱신 API (`/v1/auth/refresh`)
  - [ ] 로그아웃 API (`/v1/auth/logout`)

#### Phase 3: 사용자 관리 (1주)
- [ ] **사용자 프로필 API**
  - [ ] 프로필 조회 (`/v1/users/{userId}`)
  - [ ] 프로필 수정 (`PUT /v1/users/me`)
  - [ ] 프로필 이미지 업로드
  - [ ] 계정 삭제 (소프트 삭제)

- [ ] **팔로우 시스템**
  - [ ] 팔로우/언팔로우 (`POST/DELETE /v1/users/{userId}/follow`)
  - [ ] 팔로워 목록 (`GET /v1/users/{userId}/followers`)
  - [ ] 팔로잉 목록 (`GET /v1/users/{userId}/following`)

#### Phase 4: 보안 강화 (1주)
- [ ] **Rate Limiting**
  - [ ] Redis 기반 Rate Limiting
  - [ ] API별 요청 제한
  - [ ] IP 기반 제한

- [ ] **입력 검증**
  - [ ] Bean Validation 적용
  - [ ] XSS 방지
  - [ ] SQL Injection 방지

---

### 💬 Agent 2: Social Features Agent

#### Phase 1: 체크인 시스템 (2주)
- [ ] **체크인 CRUD**
  - [ ] 체크인 생성 (`POST /v1/check-ins`)
  - [ ] 체크인 조회 (`GET /v1/check-ins/{id}`)
  - [ ] 체크인 수정 (`PUT /v1/check-ins/{id}`)
  - [ ] 체크인 삭제 (`DELETE /v1/check-ins/{id}`)

- [ ] **체크인 테이블**
  - [ ] `check_ins` 테이블
  - [ ] `check_in_images` 테이블
  - [ ] `check_in_tags` 테이블
  - [ ] `check_in_hashtags` 테이블

#### Phase 2: 피드 시스템 (1주)
- [ ] **홈 피드**
  - [ ] 팔로우한 사용자 체크인
  - [ ] 시간순 정렬
  - [ ] 무한 스크롤 지원

- [ ] **탐색 피드**
  - [ ] 모든 공개 체크인
  - [ ] 인기 체크인
  - [ ] 추천 체크인

#### Phase 3: 소셜 인터랙션 (1주)
- [ ] **좋아요 시스템**
  - [ ] 체크인 좋아요 (`POST /v1/check-ins/{id}/like`)
  - [ ] 댓글 좋아요 (`POST /v1/comments/{id}/like`)
  - [ ] 좋아요 수 실시간 업데이트

- [ ] **댓글 시스템**
  - [ ] 댓글 CRUD (`/v1/check-ins/{id}/comments`)
  - [ ] 대댓글 지원
  - [ ] 댓글 페이지네이션

#### Phase 4: 알림 시스템 (1주)
- [ ] **알림 생성**
  - [ ] 좋아요 알림
  - [ ] 댓글 알림
  - [ ] 팔로우 알림

- [ ] **실시간 알림**
  - [ ] WebSocket 구현
  - [ ] FCM 푸시 알림
  - [ ] 알림 설정

---

### 🚀 Agent 3: Advanced Features Agent

#### Phase 1: 검색 시스템 (1주)
- [ ] **Elasticsearch 연동**
  - [ ] Elasticsearch 설정
  - [ ] 검색 인덱스 생성
  - [ ] 검색 API 구현

- [ ] **검색 기능**
  - [ ] 통합 검색 (`GET /v1/search`)
  - [ ] 사용자 검색
  - [ ] 체크인 검색
  - [ ] 위스키 검색

#### Phase 2: 태그 및 해시태그 (1주)
- [ ] **해시태그 시스템**
  - [ ] 해시태그 자동 추출
  - [ ] 트렌딩 해시태그
  - [ ] 해시태그 검색

- [ ] **위치 서비스**
  - [ ] 근처 장소 검색
  - [ ] 위치 기반 체크인
  - [ ] 지도 연동

#### Phase 3: 성능 최적화 (1주)
- [ ] **캐싱 전략**
  - [ ] Redis 클러스터
  - [ ] 캐시 무효화
  - [ ] 캐시 히트율 모니터링

- [ ] **비동기 처리**
  - [ ] 이벤트 기반 아키텍처
  - [ ] 메시지 큐 (RabbitMQ)
  - [ ] 배치 처리

#### Phase 4: 모니터링 및 배포 (1주)
- [ ] **모니터링 시스템**
  - [ ] Prometheus + Grafana
  - [ ] ELK Stack
  - [ ] 에러 추적 (Sentry)

- [ ] **CI/CD 파이프라인**
  - [ ] GitHub Actions
  - [ ] 자동 테스트
  - [ ] 자동 배포

---

## 🏗️ 워크스페이스 구조

```
spiritscribe-backend/
├── agent1-core/                 # Agent 1 작업 영역
│   ├── database/
│   │   ├── migrations/
│   │   └── schemas/
│   ├── auth/
│   │   ├── jwt/
│   │   └── security/
│   ├── user/
│   │   ├── domain/
│   │   ├── repository/
│   │   ├── service/
│   │   └── api/
│   └── README.md
├── agent2-social/               # Agent 2 작업 영역
│   ├── checkin/
│   │   ├── domain/
│   │   ├── repository/
│   │   ├── service/
│   │   └── api/
│   ├── social/
│   │   ├── like/
│   │   ├── comment/
│   │   └── share/
│   ├── notification/
│   │   ├── domain/
│   │   ├── service/
│   │   └── websocket/
│   └── README.md
├── agent3-advanced/            # Agent 3 작업 영역
│   ├── search/
│   │   ├── elasticsearch/
│   │   └── api/
│   ├── analytics/
│   │   ├── metrics/
│   │   └── reports/
│   ├── monitoring/
│   │   ├── prometheus/
│   │   └── grafana/
│   ├── deployment/
│   │   ├── docker/
│   │   └── ci-cd/
│   └── README.md
├── shared/                     # 공유 리소스
│   ├── common/
│   ├── config/
│   └── utils/
└── docs/                       # 문서화
    ├── api/
    ├── architecture/
    └── deployment/
```

---

## 🔄 Agent 간 협업 규칙

### 1. 의존성 관리
- **Agent 1 → Agent 2**: 사용자 도메인 완성 후 체크인 시스템 개발
- **Agent 2 → Agent 3**: 체크인 데이터 완성 후 검색 시스템 개발
- **공유 리소스**: `shared/` 디렉토리에서 공통 코드 관리

### 2. API 인터페이스
- **REST API**: 표준화된 엔드포인트 구조
- **데이터 전송**: JSON 형태로 통일
- **에러 처리**: 공통 에러 응답 형식

### 3. 데이터베이스 스키마
- **Agent 1**: 사용자, 인증 관련 테이블
- **Agent 2**: 체크인, 소셜 관련 테이블
- **Agent 3**: 검색, 분석 관련 테이블

### 4. 테스트 전략
- **단위 테스트**: 각 Agent별 독립 테스트
- **통합 테스트**: Agent 간 인터페이스 테스트
- **E2E 테스트**: 전체 시스템 테스트

---

## 📅 개발 일정

### Week 1-2: Agent 1 (Core Infrastructure)
- 데이터베이스 스키마 완성
- JWT 인증 시스템 구현
- 사용자 관리 API 완성

### Week 3-4: Agent 2 (Social Features)
- 체크인 시스템 구현
- 소셜 인터랙션 구현
- 알림 시스템 구현

### Week 5-6: Agent 3 (Advanced Features)
- 검색 시스템 구현
- 성능 최적화
- 모니터링 시스템 구축

### Week 7-8: 통합 및 테스트
- Agent 간 통합
- 전체 시스템 테스트
- 배포 및 문서화

---

## 🛠️ Agent 설정 가이드

### Cursor AI Agent 설정
1. **Agent 1**: Core Infrastructure 전용 워크스페이스 설정
2. **Agent 2**: Social Features 전용 워크스페이스 설정  
3. **Agent 3**: Advanced Features 전용 워크스페이스 설정

### 각 Agent별 권한
- **Agent 1**: `agent1-core/`, `shared/` 디렉토리 수정 권한
- **Agent 2**: `agent2-social/`, `shared/` 디렉토리 수정 권한
- **Agent 3**: `agent3-advanced/`, `shared/` 디렉토리 수정 권한

### 통신 프로토콜
- **Git**: 각 Agent별 브랜치에서 작업
- **Slack/Discord**: 실시간 소통
- **GitHub Issues**: 작업 진행 상황 공유

이 설정을 통해 3개의 AI Agent가 병렬로 효율적으로 프로젝트를 완성할 수 있습니다.
