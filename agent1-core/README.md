# Agent 1: Core Infrastructure

## 🎯 담당 영역
- **인증 시스템**: JWT, 회원가입, 로그인, 로그아웃
- **사용자 관리**: 프로필, 통계, 팔로우, 차단
- **데이터베이스**: 스키마 설계, 마이그레이션, 최적화
- **보안**: Spring Security, CORS, Rate Limiting
- **기본 설정**: 애플리케이션 설정, 환경 구성

## 📁 작업 디렉토리 구조
```
agent1-core/
├── auth/
│   ├── api/AuthController.kt          # 인증 API
│   ├── service/JwtTokenService.kt    # JWT 토큰 서비스
│   └── jwt/JwtTokenProvider.kt       # JWT 토큰 제공자
├── user/
│   ├── api/UserController.kt         # 사용자 API
│   ├── domain/User.kt               # 사용자 도메인
│   ├── repository/UserRepository.kt  # 사용자 저장소
│   └── service/UserService.kt        # 사용자 서비스
├── database/
│   └── migrations/                  # 데이터베이스 마이그레이션
├── config/
│   ├── SecurityConfig.kt            # 보안 설정
│   └── CorsConfig.kt                # CORS 설정
└── README.md
```

## 🚀 시작하기
1. 워크스페이스로 이동: `cd agent1-core/`
2. 작업 시작: `../agent1-work.sh`

## 📋 완료된 작업
- ✅ **인증 시스템**: JWT 토큰 기반 인증
- ✅ **사용자 관리**: 프로필 CRUD, 통계
- ✅ **데이터베이스**: 기본 스키마, 마이그레이션
- ✅ **보안**: Spring Security, CORS 설정

## 🔄 다음 작업 항목
- [ ] 팔로우/차단 시스템 구현
- [ ] 사용자 통계 자동 업데이트
- [ ] 비밀번호 재설정 기능
- [ ] 이메일 인증 시스템
- [ ] Rate Limiting 구현

## 🔗 의존성
- **Agent 2**: 사용자 도메인 완성 후 체크인 시스템 개발 가능
- **Agent 3**: 사용자 데이터 기반 검색 시스템 개발 가능

## 📅 예상 완료일
- **Week 1-2**: 데이터베이스 및 인증 시스템 ✅
- **Week 3-4**: 사용자 관리 및 보안 강화