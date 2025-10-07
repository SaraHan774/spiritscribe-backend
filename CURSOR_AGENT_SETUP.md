# Cursor AI Agent 설정 가이드

## 🤖 3개 Agent 설정 방법

### Agent 1: Core Infrastructure Agent

#### 워크스페이스 설정
```bash
# Agent 1 전용 워크스페이스 생성
mkdir -p ~/spiritscribe-agent1
cd ~/spiritscribe-agent1
git clone https://github.com/SaraHan774/spiritscribe-backend.git .
```

#### Cursor 설정
1. **Cursor에서 워크스페이스 열기**: `~/spiritscribe-agent1`
2. **Agent 프롬프트 설정**:
```
당신은 SpiritScribe 백엔드의 Core Infrastructure Agent입니다.

담당 영역:
- 데이터베이스 스키마 설계 및 구현
- JWT 인증 시스템
- 사용자 관리 시스템
- 보안 및 Rate Limiting

작업 디렉토리: agent1-core/
의존성: Agent 2, Agent 3과 협업 필요

현재 작업: [구체적인 작업 내용]
다음 단계: [다음에 할 작업]
```

#### 작업 브랜치
```bash
git checkout -b agent1-core-infrastructure
```

---

### Agent 2: Social Features Agent

#### 워크스페이스 설정
```bash
# Agent 2 전용 워크스페이스 생성
mkdir -p ~/spiritscribe-agent2
cd ~/spiritscribe-agent2
git clone https://github.com/SaraHan774/spiritscribe-backend.git .
```

#### Cursor 설정
1. **Cursor에서 워크스페이스 열기**: `~/spiritscribe-agent2`
2. **Agent 프롬프트 설정**:
```
당신은 SpiritScribe 백엔드의 Social Features Agent입니다.

담당 영역:
- 체크인 시스템 (CRUD, 피드)
- 소셜 인터랙션 (좋아요, 댓글, 공유)
- 알림 시스템 (WebSocket, FCM)
- 이미지 관리

작업 디렉토리: agent2-social/
의존성: Agent 1의 사용자 도메인 완성 필요

현재 작업: [구체적인 작업 내용]
다음 단계: [다음에 할 작업]
```

#### 작업 브랜치
```bash
git checkout -b agent2-social-features
```

---

### Agent 3: Advanced Features Agent

#### 워크스페이스 설정
```bash
# Agent 3 전용 워크스페이스 생성
mkdir -p ~/spiritscribe-agent3
cd ~/spiritscribe-agent3
git clone https://github.com/SaraHan774/spiritscribe-backend.git .
```

#### Cursor 설정
1. **Cursor에서 워크스페이스 열기**: `~/spiritscribe-agent3`
2. **Agent 프롬프트 설정**:
```
당신은 SpiritScribe 백엔드의 Advanced Features Agent입니다.

담당 영역:
- 검색 시스템 (Elasticsearch)
- 태그 및 해시태그 시스템
- 위치 서비스
- 성능 최적화
- 모니터링 및 배포

작업 디렉토리: agent3-advanced/
의존성: Agent 1, Agent 2의 데이터 완성 필요

현재 작업: [구체적인 작업 내용]
다음 단계: [다음에 할 작업]
```

#### 작업 브랜치
```bash
git checkout -b agent3-advanced-features
```

---

## 🔄 Agent 간 협업 프로토콜

### 1. Git 워크플로우
```bash
# 각 Agent별 작업
git add .
git commit -m "Agent X: [작업 내용]"
git push origin agentX-[feature-name]

# 메인 브랜치로 머지
git checkout main
git merge agentX-[feature-name]
```

### 2. 의존성 관리
- **Agent 1 → Agent 2**: 사용자 도메인 완성 후 알림
- **Agent 2 → Agent 3**: 체크인 데이터 완성 후 알림
- **공유 리소스**: `shared/` 디렉토리에서 공통 코드 관리

### 3. API 인터페이스
- **REST API**: 표준화된 엔드포인트 구조
- **데이터 전송**: JSON 형태로 통일
- **에러 처리**: 공통 에러 응답 형식

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

## 🛠️ Agent별 권한 설정

### Agent 1 권한
- `agent1-core/` 디렉토리 수정 권한
- `shared/` 디렉토리 수정 권한
- 데이터베이스 스키마 수정 권한

### Agent 2 권한
- `agent2-social/` 디렉토리 수정 권한
- `shared/` 디렉토리 수정 권한
- 체크인 관련 테이블 수정 권한

### Agent 3 권한
- `agent3-advanced/` 디렉토리 수정 권한
- `shared/` 디렉토리 수정 권한
- 검색 및 분석 관련 테이블 수정 권한

---

## 📞 Agent 간 통신

### 실시간 소통
- **Slack/Discord**: 실시간 소통 채널
- **GitHub Issues**: 작업 진행 상황 공유
- **GitHub Discussions**: 기술적 논의

### 코드 리뷰
- **Pull Request**: 각 Agent별 코드 리뷰
- **코드 품질**: 일관된 코딩 스타일 유지
- **테스트 커버리지**: 80% 이상 유지

---

## 🎯 성공 지표

### Agent 1 성공 지표
- [ ] 사용자 인증 시스템 완성
- [ ] 데이터베이스 스키마 완성
- [ ] 보안 테스트 통과

### Agent 2 성공 지표
- [ ] 체크인 시스템 완성
- [ ] 소셜 인터랙션 완성
- [ ] 알림 시스템 완성

### Agent 3 성공 지표
- [ ] 검색 시스템 완성
- [ ] 성능 최적화 완성
- [ ] 모니터링 시스템 완성

### 전체 성공 지표
- [ ] 모든 Agent 작업 통합
- [ ] 전체 시스템 테스트 통과
- [ ] 프로덕션 배포 완료

이 설정을 통해 3개의 AI Agent가 병렬로 효율적으로 프로젝트를 완성할 수 있습니다.
