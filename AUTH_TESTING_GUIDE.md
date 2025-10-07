# SpiritScribe Backend - 인증 테스트 가이드

## 📋 개요
SpiritScribe 위스키 소셜 커뮤니티의 회원가입, 로그인, 인증 기능을 테스트하는 방법을 안내합니다.

## 🌐 Swagger UI 접속

### **로컬 개발 서버**
- **URL**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🔐 인증 API 엔드포인트

### **1. 회원가입 (Sign Up)**
- **POST** `/v1/auth/signup`
- **설명**: 새로운 사용자 계정을 생성합니다.

#### **요청 예시**
```json
{
  "username": "whiskeylover",
  "email": "whiskeylover@example.com",
  "password": "password123",
  "displayName": "위스키러버",
  "bio": "위스키를 사랑하는 사람입니다",
  "location": "서울, 한국"
}
```

#### **응답 예시**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": "user_1234567890",
    "username": "whiskeylover",
    "email": "whiskeylover@example.com",
    "displayName": "위스키러버",
    "profileImageUrl": null,
    "bio": "위스키를 사랑하는 사람입니다",
    "isVerified": false,
    "isPrivate": false,
    "location": "서울, 한국",
    "website": null,
    "createdAt": "2025-10-07T05:30:00Z"
  }
}
```

### **2. 로그인 (Sign In)**
- **POST** `/v1/auth/signin`
- **설명**: 이메일과 비밀번호로 로그인합니다.

#### **요청 예시**
```json
{
  "email": "whiskeylover@example.com",
  "password": "password123"
}
```

#### **응답 예시**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": "user_123",
    "username": "whiskeylover",
    "email": "whiskeylover@example.com",
    "displayName": "위스키러버",
    "profileImageUrl": null,
    "bio": "위스키를 사랑하는 사람입니다",
    "isVerified": false,
    "isPrivate": false,
    "location": "서울, 한국",
    "website": null,
    "createdAt": "2025-10-07T05:30:00Z"
  }
}
```

### **3. 토큰 갱신 (Refresh Token)**
- **POST** `/v1/auth/refresh`
- **설명**: 리프레시 토큰으로 새로운 액세스 토큰을 발급받습니다.

#### **요청 예시**
```json
{
  "refreshToken": "refresh_token_here"
}
```

### **4. 로그아웃 (Logout)**
- **POST** `/v1/auth/logout`
- **설명**: 사용자를 로그아웃합니다.
- **인증 필요**: Bearer 토큰

### **5. 내 정보 조회 (Get Me)**
- **GET** `/v1/auth/me`
- **설명**: 현재 로그인한 사용자의 정보를 조회합니다.
- **인증 필요**: Bearer 토큰

### **6. 비밀번호 재설정 요청**
- **POST** `/v1/auth/password-reset`
- **설명**: 비밀번호 재설정을 위한 이메일을 발송합니다.

#### **요청 예시**
```json
{
  "email": "whiskeylover@example.com"
}
```

### **7. 비밀번호 재설정 확인**
- **POST** `/v1/auth/password-reset/confirm`
- **설명**: 비밀번호 재설정 토큰으로 새 비밀번호를 설정합니다.

#### **요청 예시**
```json
{
  "token": "reset_token_here",
  "newPassword": "newpassword123"
}
```

## 🧪 Swagger UI에서 테스트하는 방법

### **1. Swagger UI 접속**
1. 브라우저에서 http://localhost:8080/swagger-ui.html 접속
2. **Authentication** 섹션을 찾습니다.

### **2. 회원가입 테스트**
1. **POST** `/v1/auth/signup` 엔드포인트를 찾습니다.
2. **Try it out** 버튼을 클릭합니다.
3. 요청 본문에 회원가입 정보를 입력합니다:
   ```json
   {
     "username": "testuser",
     "email": "test@example.com",
     "password": "password123",
     "displayName": "테스트 사용자",
     "bio": "테스트 계정입니다",
     "location": "서울, 한국"
   }
   ```
4. **Execute** 버튼을 클릭합니다.
5. 응답에서 `accessToken`을 복사합니다.

### **3. 로그인 테스트**
1. **POST** `/v1/auth/signin` 엔드포인트를 찾습니다.
2. **Try it out** 버튼을 클릭합니다.
3. 요청 본문에 로그인 정보를 입력합니다:
   ```json
   {
     "email": "test@example.com",
     "password": "password123"
   }
   ```
4. **Execute** 버튼을 클릭합니다.
5. 응답에서 `accessToken`을 복사합니다.

### **4. 인증이 필요한 API 테스트**
1. **Authorize** 버튼을 클릭합니다.
2. **bearerAuth** 섹션에서 **Value** 필드에 토큰을 입력합니다:
   ```
   Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```
3. **Authorize** 버튼을 클릭합니다.
4. 이제 인증이 필요한 API를 테스트할 수 있습니다.

## 🔧 cURL로 테스트하는 방법

### **1. 회원가입**
```bash
curl -X POST "http://localhost:8080/v1/auth/signup" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "displayName": "테스트 사용자",
    "bio": "테스트 계정입니다",
    "location": "서울, 한국"
  }'
```

### **2. 로그인**
```bash
curl -X POST "http://localhost:8080/v1/auth/signin" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### **3. 인증이 필요한 API 호출**
```bash
# 토큰을 변수에 저장
TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# 내 정보 조회
curl -X GET "http://localhost:8080/v1/auth/me" \
  -H "Authorization: Bearer $TOKEN"

# 로그아웃
curl -X POST "http://localhost:8080/v1/auth/logout" \
  -H "Authorization: Bearer $TOKEN"
```

## 📱 Postman으로 테스트하는 방법

### **1. 환경 변수 설정**
- `base_url`: `http://localhost:8080`
- `access_token`: (로그인 후 받은 토큰)

### **2. Pre-request Script (로그인용)**
```javascript
// 로그인 요청 후 토큰을 환경 변수에 저장
if (pm.response.code === 200) {
    const response = pm.response.json();
    pm.environment.set("access_token", response.accessToken);
}
```

### **3. Authorization 설정**
- **Type**: Bearer Token
- **Token**: `{{access_token}}`

## 🔒 JWT 토큰 구조

### **토큰 예시**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyXzEyMyIsInVzZXJuYW1lIjoid2hpc2tleWxvdmVyIiwiaWF0IjoxNjk2NjY2NjY2LCJleHAiOjE2OTY2NzAyNjZ9.signature
```

### **페이로드 (디코딩된 내용)**
```json
{
  "sub": "user_123",
  "username": "whiskeylover",
  "iat": 1696666666,
  "exp": 1696670266
}
```

## 🚨 에러 코드 및 해결 방법

### **400 Bad Request**
- **원인**: 잘못된 요청 데이터
- **해결**: 요청 본문의 형식과 필수 필드를 확인하세요.

### **401 Unauthorized**
- **원인**: 인증 실패 또는 유효하지 않은 토큰
- **해결**: 이메일/비밀번호를 확인하거나 토큰을 갱신하세요.

### **409 Conflict**
- **원인**: 이미 존재하는 이메일 또는 사용자명
- **해결**: 다른 이메일 또는 사용자명을 사용하세요.

### **500 Internal Server Error**
- **원인**: 서버 내부 오류
- **해결**: 서버 로그를 확인하고 관리자에게 문의하세요.

## 📊 테스트 시나리오

### **1. 정상 회원가입 플로우**
1. 회원가입 API 호출
2. 응답에서 토큰 확인
3. 인증이 필요한 API 호출 테스트

### **2. 로그인 플로우**
1. 로그인 API 호출
2. 응답에서 토큰 확인
3. 토큰으로 보호된 리소스 접근

### **3. 토큰 갱신 플로우**
1. 리프레시 토큰으로 새 액세스 토큰 발급
2. 새 토큰으로 API 호출

### **4. 비밀번호 재설정 플로우**
1. 비밀번호 재설정 요청
2. 이메일 확인 (실제 구현에서는)
3. 재설정 토큰으로 새 비밀번호 설정

## 🎯 테스트 체크리스트

### **회원가입 테스트**
- [ ] 유효한 데이터로 회원가입 성공
- [ ] 중복 이메일로 회원가입 실패
- [ ] 중복 사용자명으로 회원가입 실패
- [ ] 필수 필드 누락 시 에러 처리

### **로그인 테스트**
- [ ] 유효한 이메일/비밀번호로 로그인 성공
- [ ] 잘못된 비밀번호로 로그인 실패
- [ ] 존재하지 않는 이메일로 로그인 실패

### **인증 테스트**
- [ ] 유효한 토큰으로 보호된 리소스 접근
- [ ] 유효하지 않은 토큰으로 접근 실패
- [ ] 만료된 토큰으로 접근 실패

### **토큰 관리 테스트**
- [ ] 토큰 갱신 기능
- [ ] 로그아웃 기능
- [ ] 토큰 만료 처리

## 🔧 개발자 도구

### **JWT 디버거**
- **JWT.io**: https://jwt.io/
- 토큰 디코딩 및 검증

### **API 테스트 도구**
- **Postman**: https://www.postman.com/
- **Insomnia**: https://insomnia.rest/
- **Thunder Client**: VS Code 확장

## 📚 추가 리소스

- **Spring Security**: https://spring.io/projects/spring-security
- **JWT**: https://jwt.io/
- **OpenAPI**: https://swagger.io/specification/
- **SpringDoc**: https://springdoc.org/

## 🎉 결론

이제 SpiritScribe Backend의 인증 시스템을 완전히 테스트할 수 있습니다!

**주요 특징:**
- ✅ **완전한 인증 플로우**
- ✅ **JWT 토큰 기반 보안**
- ✅ **Swagger UI 통합 테스트**
- ✅ **다양한 클라이언트 지원**
- ✅ **상세한 에러 처리**

**테스트 시작**: http://localhost:8080/swagger-ui.html 🚀
