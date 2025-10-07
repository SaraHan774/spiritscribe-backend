# SpiritScribe Backend API 문서

## 📋 개요
SpiritScribe 위스키 소셜 커뮤니티의 백엔드 API 문서입니다. Swagger UI를 통해 모든 API를 테스트하고 공유할 수 있습니다.

## 🌐 API 접근 방법

### **Swagger UI 접근**
- **로컬 개발**: http://localhost:8080/swagger-ui.html
- **개발 서버**: https://dev-api.spiritscribe.com/swagger-ui.html
- **프로덕션**: https://api.spiritscribe.com/swagger-ui.html

### **OpenAPI JSON**
- **로컬 개발**: http://localhost:8080/v3/api-docs
- **개발 서버**: https://dev-api.spiritscribe.com/v3/api-docs
- **프로덕션**: https://api.spiritscribe.com/v3/api-docs

## 🚀 서버 실행 방법

### **1. Docker 서비스 시작**
```bash
# PostgreSQL과 Redis 컨테이너 시작
docker compose up -d

# 서비스 상태 확인
docker compose ps
```

### **2. 애플리케이션 실행**
```bash
# Gradle로 실행
./gradlew bootRun

# 또는 JAR 파일로 실행
./gradlew build
java -jar build/libs/spiritscribe-backend-1.0.0.jar
```

### **3. Swagger UI 확인**
브라우저에서 http://localhost:8080/swagger-ui.html 접속

## 📊 API 엔드포인트

### **1. Health Check**
- **GET** `/v1/health` - 서버 상태 확인

### **2. User API**
- **GET** `/v1/users/{userId}` - 사용자 프로필 조회
- **GET** `/v1/users/me` - 내 프로필 조회
- **PUT** `/v1/users/me` - 내 프로필 수정

### **3. CheckIn API**
- **POST** `/v1/check-ins` - 체크인 생성
- **GET** `/v1/check-ins/{checkInId}` - 체크인 조회
- **GET** `/v1/check-ins` - 체크인 피드 조회
- **PUT** `/v1/check-ins/{checkInId}` - 체크인 수정
- **DELETE** `/v1/check-ins/{checkInId}` - 체크인 삭제

### **4. Social API**
- **POST** `/v1/check-ins/{checkInId}/like` - 체크인 좋아요
- **DELETE** `/v1/check-ins/{checkInId}/like` - 체크인 좋아요 취소
- **POST** `/v1/check-ins/{checkInId}/comments` - 댓글 추가
- **GET** `/v1/check-ins/{checkInId}/comments` - 댓글 목록 조회
- **PUT** `/v1/comments/{commentId}` - 댓글 수정
- **DELETE** `/v1/comments/{commentId}` - 댓글 삭제
- **POST** `/v1/check-ins/{checkInId}/share` - 체크인 공유

## 🔧 Swagger UI 기능

### **1. API 테스트**
- **Try it out** 버튼으로 실제 API 호출 가능
- **Request/Response** 예시 자동 생성
- **인증 토큰** 설정 가능

### **2. 문서화 기능**
- **API 설명** 자동 생성
- **요청/응답 스키마** 자동 생성
- **에러 코드** 설명 포함

### **3. 공유 기능**
- **OpenAPI JSON** 다운로드
- **Postman Collection** 생성 가능
- **코드 생성** (다양한 언어 지원)

## 📝 API 사용 예시

### **1. 헬스 체크**
```bash
curl -X GET "http://localhost:8080/v1/health" \
  -H "accept: application/json"
```

**응답:**
```json
{
  "status": "ok"
}
```

### **2. 사용자 프로필 조회**
```bash
curl -X GET "http://localhost:8080/v1/users/user123" \
  -H "accept: application/json"
```

**응답:**
```json
{
  "id": "user123",
  "username": "whiskeylover",
  "displayName": "위스키러버",
  "profileImageUrl": null,
  "bio": "위스키를 사랑하는 사람입니다",
  "isVerified": false,
  "isPrivate": false,
  "location": "서울, 한국",
  "website": null,
  "stats": {
    "checkInsCount": 156,
    "followersCount": 1240,
    "followingCount": 890,
    "reviewsCount": 98,
    "favoritesCount": 45,
    "averageRating": 4.2
  }
}
```

### **3. 체크인 생성**
```bash
curl -X POST "http://localhost:8080/v1/check-ins" \
  -H "accept: application/json" \
  -H "Content-Type: application/json" \
  -d '{
    "whiskeyId": "whiskey_456",
    "location": "강남 위스키바",
    "locationLat": 37.5665,
    "locationLng": 126.9780,
    "locationType": "WHISKEY_BAR",
    "rating": 4.5,
    "notes": "정말 부드럽고 복잡한 맛이에요!",
    "tags": ["싱글몰트", "스모키", "프리미엄"],
    "isPublic": true
  }'
```

### **4. 체크인 좋아요**
```bash
curl -X POST "http://localhost:8080/v1/check-ins/checkin_789/like" \
  -H "accept: application/json"
```

## 🔐 인증 설정

### **JWT 토큰 사용**
Swagger UI에서 **Authorize** 버튼을 클릭하여 JWT 토큰을 설정할 수 있습니다.

**토큰 형식:**
```
Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 📊 모니터링

### **Actuator 엔드포인트**
- **Health**: http://localhost:8080/actuator/health
- **Info**: http://localhost:8080/actuator/info
- **Metrics**: http://localhost:8080/actuator/prometheus

### **Swagger UI 설정**
- **Operations Sorter**: HTTP 메서드별 정렬
- **Tags Sorter**: 알파벳순 정렬
- **Try it out**: 활성화됨
- **Show Actuator**: 활성화됨

## 🚀 배포 및 공유

### **1. GitHub Pages 배포**
```bash
# OpenAPI JSON을 GitHub Pages에 배포
curl -X GET "http://localhost:8080/v3/api-docs" > api-docs.json
```

### **2. Postman Collection 생성**
```bash
# Postman Collection으로 변환
curl -X GET "http://localhost:8080/v3/api-docs" | \
  jq '.info.title = "SpiritScribe API"' > postman-collection.json
```

### **3. 코드 생성**
```bash
# OpenAPI Generator로 클라이언트 코드 생성
npx @openapitools/openapi-generator-cli generate \
  -i http://localhost:8080/v3/api-docs \
  -g typescript-axios \
  -o ./client
```

## 📈 성능 및 최적화

### **1. 캐싱**
- Redis를 통한 API 응답 캐싱
- CDN을 통한 정적 자산 캐싱

### **2. 압축**
- Gzip 압축 활성화
- 최소 응답 크기: 2KB

### **3. CORS**
- 허용된 Origin: localhost:3000, dev.spiritscribe.com
- 허용된 메서드: GET, POST, PUT, DELETE, PATCH, OPTIONS
- 인증 정보 포함 가능

## 🔧 문제 해결

### **1. Swagger UI 접근 불가**
```bash
# 서버 상태 확인
curl -X GET "http://localhost:8080/v1/health"

# 포트 확인
netstat -an | grep 8080
```

### **2. API 호출 실패**
```bash
# 로그 확인
tail -f logs/application.log

# 데이터베이스 연결 확인
docker compose ps
```

### **3. CORS 오류**
```bash
# CORS 설정 확인
curl -X OPTIONS "http://localhost:8080/v1/health" \
  -H "Origin: http://localhost:3000"
```

## 📚 추가 리소스

- **Spring Boot**: https://spring.io/projects/spring-boot
- **OpenAPI 3.0**: https://swagger.io/specification/
- **SpringDoc OpenAPI**: https://springdoc.org/
- **PostgreSQL**: https://www.postgresql.org/
- **Redis**: https://redis.io/

## 🎉 결론

SpiritScribe Backend API는 Swagger UI를 통해 완전히 문서화되어 있으며, 개발자들이 쉽게 테스트하고 통합할 수 있습니다.

**주요 특징:**
- ✅ **완전한 API 문서화**
- ✅ **실시간 API 테스트**
- ✅ **자동 코드 생성**
- ✅ **다양한 클라이언트 지원**
- ✅ **모니터링 및 디버깅**

**접속 URL**: http://localhost:8080/swagger-ui.html
