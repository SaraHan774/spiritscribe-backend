# SpiritScribe Backend (Skeleton)

Kotlin + Spring Boot 3.x 기반의 SpiritScribe 백엔드 스켈레톤 프로젝트입니다.

## 요구 사항
- JDK 21
- Docker / Docker Compose (선택)

## 빠른 시작
1) 의존 서비스 실행: `docker compose up -d`
2) 애플리케이션 실행: `./gradlew bootRun`
3) 헬스 체크: `http://localhost:8080/v1/health`

## 주요 구성
- Spring Boot: Web, Security, Validation, WebSocket, Actuator
- 데이터: Spring Data JPA, PostgreSQL, Redis
- 마이그레이션: Flyway (V1__baseline.sql)
- 프로필: `application-local.yml`, `application-dev.yml`, `application-prod.yml`

## 디렉터리
- `src/main/kotlin/com/spiritscribe`: 애플리케이션 코드
- `src/main/resources`: 설정 및 마이그레이션

## 다음 단계
- JWT 인증 필터 추가 및 보안 강화
- 도메인 엔티티/리포지토리/서비스 확장
- OpenAPI 문서화 및 테스트 코드 작성
