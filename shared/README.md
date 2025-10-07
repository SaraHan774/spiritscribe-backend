# Shared Resources

## 🎯 공유 리소스
- **공통 설정**: OpenAPI, WebSocket, 공통 설정
- **유틸리티**: 공통 유틸리티, 헬퍼 함수
- **문서**: API 문서, 배포 가이드, 공통 문서
- **테스트**: 공통 테스트 유틸리티

## 📁 작업 디렉토리 구조
```
shared/
├── config/
│   ├── OpenApiConfig.kt              # OpenAPI 설정
│   └── WebSocketConfig.kt            # WebSocket 설정
├── common/
│   └── utils/CommonUtils.kt          # 공통 유틸리티
├── docs/
│   ├── API_DOCUMENTATION.md          # API 문서
│   └── DEPLOYMENT_GUIDE.md           # 배포 가이드
└── README.md
```

## 🚀 사용 방법
1. 모든 Agent에서 공통으로 사용
2. 설정 변경 시 모든 Agent에 영향
3. 문서 업데이트 시 모든 Agent 참조

## 📋 완료된 작업
- ✅ **OpenAPI 설정**: Swagger UI 통합
- ✅ **WebSocket 설정**: 실시간 통신 준비

## 🔄 다음 작업 항목
- [ ] 공통 유틸리티 함수
- [ ] 통합 API 문서
- [ ] 배포 가이드
- [ ] 공통 테스트 유틸리티

## 🔗 사용하는 Agent
- **Agent 1**: OpenAPI, WebSocket 설정 사용
- **Agent 2**: WebSocket, 공통 유틸리티 사용
- **Agent 3**: OpenAPI, 모니터링 설정 사용