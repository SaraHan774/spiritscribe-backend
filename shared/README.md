# Shared Resources

## 🎯 공유 리소스
- 공통 도메인 모델
- 공통 유틸리티
- 공통 설정
- 공통 예외 처리

## 📁 구조
```
shared/
├── common/
│   ├── domain/
│   ├── dto/
│   └── exception/
├── config/
│   ├── database/
│   ├── redis/
│   └── security/
└── utils/
    ├── validation/
    ├── conversion/
    └── helper/
```

## 🔄 사용 규칙
- 모든 Agent가 공유 리소스 사용 가능
- 수정 시 다른 Agent와 협의 필요
- 버전 관리 필수
