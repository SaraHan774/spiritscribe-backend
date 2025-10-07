#!/bin/bash

# SpiritScribe Backend - AI Agent 자동 완성 시스템
echo "🤖 SpiritScribe Backend - AI Agent 자동 완성 시작"
echo "=================================================="

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
PURPLE='\033[0;35m'
NC='\033[0m'

# 프로젝트 루트 디렉토리
PROJECT_ROOT="/Users/gahee/spiritscribe-backend"
LOG_DIR="$PROJECT_ROOT/logs"
mkdir -p "$LOG_DIR"

# Agent별 로그 파일
AGENT1_LOG="$LOG_DIR/agent1.log"
AGENT2_LOG="$LOG_DIR/agent2.log"
AGENT3_LOG="$LOG_DIR/agent3.log"
MAIN_LOG="$LOG_DIR/main.log"

# 로그 함수
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a "$MAIN_LOG"
}

log_success() {
    log "${GREEN}✅ $1${NC}"
}

log_error() {
    log "${RED}❌ $1${NC}"
}

log_info() {
    log "${BLUE}ℹ️ $1${NC}"
}

log_warning() {
    log "${YELLOW}⚠️ $1${NC}"
}

# Agent 작업 완료 확인 함수
check_agent_completion() {
    local agent_num=$1
    local agent_log=$2
    local expected_files=$3
    
    if [ -f "$agent_log" ]; then
        local completion_count=$(grep -c "✅.*작업 완료" "$agent_log" 2>/dev/null || echo "0")
        local file_count=$(find "agent${agent_num}-*" -name "*.kt" -o -name "*.sql" 2>/dev/null | wc -l)
        
        if [ "$completion_count" -gt 0 ] && [ "$file_count" -ge "$expected_files" ]; then
            return 0  # 완료
        fi
    fi
    return 1  # 미완료
}

# Agent 작업 실행 함수
execute_agent_work() {
    local agent_num=$1
    local agent_name=$2
    local expected_files=$3
    local max_attempts=3
    local attempt=1
    
    log_info "🤖 $agent_name 작업 시작 (시도 $attempt/$max_attempts)"
    
    while [ $attempt -le $max_attempts ]; do
        # Agent 작업 실행
        if [ $agent_num -eq 1 ]; then
            ./agent1-work.sh > "$AGENT1_LOG" 2>&1 &
        elif [ $agent_num -eq 2 ]; then
            ./agent2-work.sh > "$AGENT2_LOG" 2>&1 &
        elif [ $agent_num -eq 3 ]; then
            ./agent3-work.sh > "$AGENT3_LOG" 2>&1 &
        fi
        
        local agent_pid=$!
        log_info "Agent $agent_num PID: $agent_pid"
        
        # 작업 완료 대기 (최대 5분)
        local wait_time=0
        while [ $wait_time -lt 300 ]; do
            if check_agent_completion $agent_num "agent${agent_num}.log" $expected_files; then
                log_success "$agent_name 작업 완료"
                return 0
            fi
            
            sleep 10
            wait_time=$((wait_time + 10))
            
            # 진행 상황 로그
            if [ $((wait_time % 60)) -eq 0 ]; then
                log_info "$agent_name 작업 진행 중... (${wait_time}초 경과)"
            fi
        done
        
        # 작업 완료 확인
        if check_agent_completion $agent_num "agent${agent_num}.log" $expected_files; then
            log_success "$agent_name 작업 완료"
            return 0
        else
            log_warning "$agent_name 작업 미완료, 재시도 중... ($attempt/$max_attempts)"
            attempt=$((attempt + 1))
            
            # Agent 프로세스 종료
            kill $agent_pid 2>/dev/null
            sleep 5
        fi
    done
    
    log_error "$agent_name 작업 실패 (최대 시도 횟수 초과)"
    return 1
}

# 코드 품질 검사 함수
check_code_quality() {
    local agent_num=$1
    local agent_name=$2
    
    log_info "🔍 $agent_name 코드 품질 검사 시작"
    
    # Kotlin 파일 검사
    local kotlin_files=$(find "agent${agent_num}-*" -name "*.kt" 2>/dev/null)
    if [ -n "$kotlin_files" ]; then
        local file_count=$(echo "$kotlin_files" | wc -l)
        log_success "$agent_name: $file_count개 Kotlin 파일 생성"
        
        # 각 파일 검사
        echo "$kotlin_files" | while read -r file; do
            if [ -f "$file" ]; then
                local line_count=$(wc -l < "$file")
                if [ "$line_count" -gt 10 ]; then
                    log_success "  ✅ $file: $line_count줄 (양호)"
                else
                    log_warning "  ⚠️ $file: $line_count줄 (짧음)"
                fi
            fi
        done
    else
        log_warning "$agent_name: Kotlin 파일이 생성되지 않았습니다"
    fi
    
    # SQL 파일 검사
    local sql_files=$(find "agent${agent_num}-*" -name "*.sql" 2>/dev/null)
    if [ -n "$sql_files" ]; then
        local sql_count=$(echo "$sql_files" | wc -l)
        log_success "$agent_name: $sql_count개 SQL 파일 생성"
    fi
}

# 통합 테스트 함수
run_integration_test() {
    log_info "🧪 통합 테스트 시작"
    
    # 1. 데이터베이스 연결 테스트
    log_info "데이터베이스 연결 테스트"
    if docker compose ps | grep -q "spiritscribe-postgres.*Up"; then
        log_success "PostgreSQL 연결 성공"
    else
        log_warning "PostgreSQL 연결 실패, 컨테이너 시작 중..."
        docker compose up -d postgres
        sleep 10
    fi
    
    # 2. Redis 연결 테스트
    log_info "Redis 연결 테스트"
    if docker compose ps | grep -q "spiritscribe-redis.*Up"; then
        log_success "Redis 연결 성공"
    else
        log_warning "Redis 연결 실패, 컨테이너 시작 중..."
        docker compose up -d redis
        sleep 5
    fi
    
    # 3. 애플리케이션 빌드 테스트
    log_info "애플리케이션 빌드 테스트"
    if ./gradlew build --no-daemon > "$LOG_DIR/build.log" 2>&1; then
        log_success "애플리케이션 빌드 성공"
    else
        log_error "애플리케이션 빌드 실패"
        log_error "빌드 로그:"
        tail -20 "$LOG_DIR/build.log" | while read -r line; do
            log_error "  $line"
        done
        return 1
    fi
    
    # 4. 애플리케이션 실행 테스트
    log_info "애플리케이션 실행 테스트"
    ./gradlew bootRun --no-daemon > "$LOG_DIR/run.log" 2>&1 &
    local app_pid=$!
    
    # 애플리케이션 시작 대기
    sleep 30
    
    # 헬스 체크
    if curl -s http://localhost:8080/v1/health | grep -q "ok"; then
        log_success "애플리케이션 실행 성공"
        kill $app_pid 2>/dev/null
        return 0
    else
        log_error "애플리케이션 실행 실패"
        kill $app_pid 2>/dev/null
        return 1
    fi
}

# 메인 실행 함수
main() {
    log_info "🚀 SpiritScribe Backend 자동 완성 시작"
    
    # 1. 환경 설정
    log_info "환경 설정 중..."
    mkdir -p agent1-core agent2-social agent3-advanced shared logs
    
    # 2. Agent 1 작업 (Core Infrastructure)
    log_info "📊 Phase 1: Core Infrastructure Agent 작업 시작"
    if execute_agent_work 1 "Core Infrastructure Agent" 5; then
        check_code_quality 1 "Core Infrastructure Agent"
        log_success "✅ Phase 1 완료: 데이터베이스, 인증, 사용자 관리"
    else
        log_error "❌ Phase 1 실패: Core Infrastructure Agent"
        return 1
    fi
    
    # 3. Agent 2 작업 (Social Features)
    log_info "📊 Phase 2: Social Features Agent 작업 시작"
    if execute_agent_work 2 "Social Features Agent" 8; then
        check_code_quality 2 "Social Features Agent"
        log_success "✅ Phase 2 완료: 체크인, 소셜 인터랙션, 알림"
    else
        log_error "❌ Phase 2 실패: Social Features Agent"
        return 1
    fi
    
    # 4. Agent 3 작업 (Advanced Features)
    log_info "📊 Phase 3: Advanced Features Agent 작업 시작"
    if execute_agent_work 3 "Advanced Features Agent" 6; then
        check_code_quality 3 "Advanced Features Agent"
        log_success "✅ Phase 3 완료: 검색, 성능 최적화, 모니터링"
    else
        log_error "❌ Phase 3 실패: Advanced Features Agent"
        return 1
    fi
    
    # 5. 통합 테스트
    log_info "📊 Phase 4: 통합 테스트 시작"
    if run_integration_test; then
        log_success "✅ Phase 4 완료: 통합 테스트 성공"
    else
        log_error "❌ Phase 4 실패: 통합 테스트 실패"
        return 1
    fi
    
    # 6. 최종 결과 리포트
    log_info "📊 최종 결과 리포트 생성 중..."
    
    # 생성된 파일 통계
    local total_kt_files=$(find . -name "*.kt" | grep -v ".git" | wc -l)
    local total_sql_files=$(find . -name "*.sql" | grep -v ".git" | wc -l)
    local total_yml_files=$(find . -name "*.yml" | grep -v ".git" | wc -l)
    
    log_success "🎉 SpiritScribe Backend 자동 완성 성공!"
    log_info "📈 생성된 파일 통계:"
    log_info "  - Kotlin 파일: $total_kt_files개"
    log_info "  - SQL 파일: $total_sql_files개"
    log_info "  - YAML 파일: $total_yml_files개"
    
    # GitHub에 푸시
    log_info "📤 GitHub에 결과 푸시 중..."
    git add .
    git commit -m "AI Agent 자동 완성: SpiritScribe Backend 완성

- Core Infrastructure: 데이터베이스, 인증, 사용자 관리
- Social Features: 체크인, 소셜 인터랙션, 알림
- Advanced Features: 검색, 성능 최적화, 모니터링
- 통합 테스트 완료
- 총 $total_kt_files개 Kotlin 파일, $total_sql_files개 SQL 파일 생성"
    
    if git push origin main; then
        log_success "✅ GitHub 푸시 성공"
    else
        log_warning "⚠️ GitHub 푸시 실패"
    fi
    
    log_success "🎉 SpiritScribe Backend 자동 완성 완료!"
    log_info "📁 로그 파일: $LOG_DIR/"
    log_info "🌐 GitHub: https://github.com/SaraHan774/spiritscribe-backend"
    
    return 0
}

# 에러 처리
trap 'log_error "스크립트 실행 중 오류 발생"; exit 1' ERR

# 메인 실행
main "$@"
