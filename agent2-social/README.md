# Agent 2: Social Features

## 🎯 담당 영역
- **체크인 시스템**: CRUD, 피드, 위치 기반 검색
- **소셜 인터랙션**: 좋아요, 댓글, 공유, 팔로우
- **알림 시스템**: WebSocket, FCM 푸시 알림
- **이미지 관리**: 업로드, 리사이징, 스토리지
- **태그 시스템**: 해시태그, 트렌딩 분석

## 📁 작업 디렉토리 구조
```
agent2-social/
├── checkin/
│   ├── api/CheckInController.kt      # 체크인 API
│   └── domain/CheckIn.kt            # 체크인 도메인
├── social/
│   ├── api/SocialController.kt       # 소셜 API
│   └── like/Like.kt                 # 좋아요 도메인
├── notification/
│   ├── api/NotificationController.kt # 알림 API
│   └── domain/Notification.kt       # 알림 도메인
├── image/
│   └── service/ImageService.kt       # 이미지 서비스
└── README.md
```

## 🚀 시작하기
1. 워크스페이스로 이동: `cd agent2-social/`
2. 작업 시작: `../agent2-work.sh`

## 📋 완료된 작업
- ✅ **체크인 시스템**: 기본 CRUD API
- ✅ **소셜 인터랙션**: 좋아요, 댓글, 공유 API
- ✅ **알림 도메인**: 기본 알림 모델

## 🔄 다음 작업 항목
- [ ] 체크인 피드 시스템 구현
- [ ] 실시간 알림 시스템 (WebSocket)
- [ ] 이미지 업로드 및 리사이징
- [ ] 해시태그 시스템
- [ ] 위치 기반 검색
- [ ] 트렌딩 분석

## 🔗 의존성
- **Agent 1**: 사용자 도메인 완성 필요
- **Agent 3**: 검색 시스템과 연동

## 📅 예상 완료일
- **Week 1-2**: 체크인 및 소셜 인터랙션
- **Week 3-4**: 알림 및 이미지 관리