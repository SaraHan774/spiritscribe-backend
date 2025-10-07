# Agent 2: Social Features Agent

## 🎯 담당 영역
- 체크인 시스템 (CRUD, 피드)
- 소셜 인터랙션 (좋아요, 댓글, 공유)
- 알림 시스템 (WebSocket, FCM)
- 이미지 관리

## 📋 주요 작업 목록

### Phase 1: 체크인 시스템 (2주)
- [ ] 체크인 CRUD API
- [ ] 체크인 테이블 설계
- [ ] 이미지 업로드 시스템
- [ ] 태그 및 해시태그

### Phase 2: 피드 시스템 (1주)
- [ ] 홈 피드 구현
- [ ] 탐색 피드 구현
- [ ] 무한 스크롤 지원
- [ ] 캐싱 최적화

### Phase 3: 소셜 인터랙션 (1주)
- [ ] 좋아요 시스템
- [ ] 댓글 시스템
- [ ] 공유 시스템
- [ ] 실시간 업데이트

### Phase 4: 알림 시스템 (1주)
- [ ] 알림 생성 로직
- [ ] WebSocket 구현
- [ ] FCM 푸시 알림
- [ ] 알림 설정

## 🏗️ 워크스페이스 구조
```
agent2-social/
├── checkin/
│   ├── domain/
│   ├── repository/
│   ├── service/
│   └── api/
├── social/
│   ├── like/
│   ├── comment/
│   └── share/
├── notification/
│   ├── domain/
│   ├── service/
│   └── websocket/
└── README.md
```

## 🔗 의존성
- **Agent 1**: 사용자 도메인 완성 필요
- **Agent 3**: 체크인 데이터 기반 검색 시스템 개발 가능

## 📅 예상 완료일
- **Week 3-4**: 체크인 시스템 구현
- **Week 5-6**: 소셜 인터랙션 및 알림 시스템
