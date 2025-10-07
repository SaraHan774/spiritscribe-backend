# Agent 1: Core Infrastructure Agent

## 🎯 담당 영역
- 데이터베이스 스키마 설계 및 구현
- JWT 인증 시스템
- 사용자 관리 시스템
- 보안 및 Rate Limiting

## 📋 주요 작업 목록

### Phase 1: 데이터베이스 스키마 (1주)
- [ ] 사용자 관련 테이블 설계
- [ ] Flyway 마이그레이션 작성
- [ ] 인덱스 및 제약조건 설정

### Phase 2: JWT 인증 시스템 (1주)
- [ ] JWT 토큰 생성/검증
- [ ] 인증 필터 구현
- [ ] 회원가입/로그인 API

### Phase 3: 사용자 관리 (1주)
- [ ] 사용자 프로필 API
- [ ] 팔로우 시스템
- [ ] 사용자 검색

### Phase 4: 보안 강화 (1주)
- [ ] Rate Limiting 구현
- [ ] 입력 검증
- [ ] 보안 테스트

## 🏗️ 워크스페이스 구조
```
agent1-core/
├── database/
│   ├── migrations/
│   └── schemas/
├── auth/
│   ├── jwt/
│   └── security/
├── user/
│   ├── domain/
│   ├── repository/
│   ├── service/
│   └── api/
└── README.md
```

## 🔗 의존성
- **Agent 2**: 사용자 도메인 완성 후 체크인 시스템 개발 가능
- **Agent 3**: 사용자 데이터 기반 검색 시스템 개발 가능

## 📅 예상 완료일
- **Week 1-2**: 데이터베이스 및 인증 시스템
- **Week 3-4**: 사용자 관리 및 보안 강화
