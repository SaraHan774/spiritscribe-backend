#!/bin/bash

# SpiritScribe Backend - 3 Agent 동시 실행 스크립트
# 이 스크립트는 3개의 Cursor AI Agent를 동시에 실행합니다.

echo "🚀 SpiritScribe Backend - 3 Agent 동시 실행 시작"
echo "=================================================="

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 프로젝트 루트 디렉토리
PROJECT_ROOT="/Users/gahee/spiritscribe-backend"

# Agent별 워크스페이스 디렉토리
AGENT1_DIR="$PROJECT_ROOT/agent1-core"
AGENT2_DIR="$PROJECT_ROOT/agent2-social"
AGENT3_DIR="$PROJECT_ROOT/agent3-advanced"

# Agent별 설정 파일
AGENT1_CONFIG="$PROJECT_ROOT/.cursor/agent1-config.json"
AGENT2_CONFIG="$PROJECT_ROOT/.cursor/agent2-config.json"
AGENT3_CONFIG="$PROJECT_ROOT/.cursor/agent3-config.json"

# Agent별 설정 파일 생성
mkdir -p "$PROJECT_ROOT/.cursor"

# Agent 1 설정
cat > "$AGENT1_CONFIG" << 'EOF'
{
  "agent": {
    "name": "Core Infrastructure Agent",
    "role": "데이터베이스, 인증, 사용자 관리, 보안",
    "workspace": "agent1-core/",
    "dependencies": [],
    "outputs": ["사용자 도메인", "인증 시스템", "데이터베이스 스키마"]
  },
  "tasks": [
    "데이터베이스 스키마 설계 및 구현",
    "JWT 인증 시스템 구현",
    "사용자 관리 API 구현",
    "보안 및 Rate Limiting 구현"
  ],
  "collaboration": {
    "notify": ["agent2", "agent3"],
    "wait_for": [],
    "deliverables": ["사용자 도메인 완성", "인증 시스템 완성"]
  }
}
EOF

# Agent 2 설정
cat > "$AGENT2_CONFIG" << 'EOF'
{
  "agent": {
    "name": "Social Features Agent",
    "role": "체크인, 소셜 인터랙션, 알림, 이미지 관리",
    "workspace": "agent2-social/",
    "dependencies": ["agent1"],
    "outputs": ["체크인 시스템", "소셜 인터랙션", "알림 시스템"]
  },
  "tasks": [
    "체크인 시스템 구현",
    "소셜 인터랙션 구현",
    "알림 시스템 구현",
    "이미지 관리 시스템 구현"
  ],
  "collaboration": {
    "notify": ["agent3"],
    "wait_for": ["agent1"],
    "deliverables": ["체크인 시스템 완성", "소셜 기능 완성"]
  }
}
EOF

# Agent 3 설정
cat > "$AGENT3_CONFIG" << 'EOF'
{
  "agent": {
    "name": "Advanced Features Agent",
    "role": "검색, 태그, 위치, 성능, 모니터링, 배포",
    "workspace": "agent3-advanced/",
    "dependencies": ["agent1", "agent2"],
    "outputs": ["검색 시스템", "성능 최적화", "모니터링 시스템"]
  },
  "tasks": [
    "검색 시스템 구현",
    "태그 및 해시태그 시스템 구현",
    "위치 서비스 구현",
    "성능 최적화 구현",
    "모니터링 시스템 구현"
  ],
  "collaboration": {
    "notify": [],
    "wait_for": ["agent1", "agent2"],
    "deliverables": ["검색 시스템 완성", "성능 최적화 완성"]
  }
}
EOF

echo -e "${GREEN}✅ Agent 설정 파일 생성 완료${NC}"

# Agent별 워크스페이스 디렉토리 생성
mkdir -p "$AGENT1_DIR"
mkdir -p "$AGENT2_DIR"
mkdir -p "$AGENT3_DIR"

echo -e "${GREEN}✅ Agent 워크스페이스 디렉토리 생성 완료${NC}"

# Agent별 작업 스크립트 생성
create_agent_script() {
    local agent_num=$1
    local agent_name=$2
    local agent_dir=$3
    local config_file=$4
    
    cat > "$PROJECT_ROOT/agent${agent_num}-work.sh" << EOF
#!/bin/bash

# Agent ${agent_num}: ${agent_name} 작업 스크립트
echo -e "${BLUE}🤖 Agent ${agent_num}: ${agent_name} 시작${NC}"
echo "=================================================="

# 워크스페이스로 이동
cd "$agent_dir"

# Cursor CLI로 Agent 실행
cursor --agent-config="$config_file" --workspace="$agent_dir" --auto-start

echo -e "${GREEN}✅ Agent ${agent_num} 작업 완료${NC}"
EOF

    chmod +x "$PROJECT_ROOT/agent${agent_num}-work.sh"
}

# Agent별 작업 스크립트 생성
create_agent_script 1 "Core Infrastructure Agent" "$AGENT1_DIR" "$AGENT1_CONFIG"
create_agent_script 2 "Social Features Agent" "$AGENT2_DIR" "$AGENT2_CONFIG"
create_agent_script 3 "Advanced Features Agent" "$AGENT3_DIR" "$AGENT3_CONFIG"

echo -e "${GREEN}✅ Agent 작업 스크립트 생성 완료${NC}"

# 병렬 실행 함수
run_agents_parallel() {
    echo -e "${YELLOW}🚀 3개 Agent 동시 실행 시작...${NC}"
    
    # Agent 1 실행 (백그라운드)
    echo -e "${BLUE}🤖 Agent 1: Core Infrastructure Agent 시작${NC}"
    ./agent1-work.sh &
    AGENT1_PID=$!
    
    # Agent 2 실행 (백그라운드)
    echo -e "${BLUE}🤖 Agent 2: Social Features Agent 시작${NC}"
    ./agent2-work.sh &
    AGENT2_PID=$!
    
    # Agent 3 실행 (백그라운드)
    echo -e "${BLUE}🤖 Agent 3: Advanced Features Agent 시작${NC}"
    ./agent3-work.sh &
    AGENT3_PID=$!
    
    echo -e "${GREEN}✅ 모든 Agent가 백그라운드에서 실행 중${NC}"
    echo -e "${YELLOW}📊 Agent 상태 모니터링:${NC}"
    echo "Agent 1 PID: $AGENT1_PID"
    echo "Agent 2 PID: $AGENT2_PID"
    echo "Agent 3 PID: $AGENT3_PID"
    
    # Agent 상태 모니터링
    monitor_agents() {
        while true; do
            echo -e "${YELLOW}📊 Agent 상태 확인 중...${NC}"
            
            # Agent 1 상태 확인
            if ps -p $AGENT1_PID > /dev/null 2>&1; then
                echo -e "${GREEN}✅ Agent 1: 실행 중${NC}"
            else
                echo -e "${RED}❌ Agent 1: 종료됨${NC}"
            fi
            
            # Agent 2 상태 확인
            if ps -p $AGENT2_PID > /dev/null 2>&1; then
                echo -e "${GREEN}✅ Agent 2: 실행 중${NC}"
            else
                echo -e "${RED}❌ Agent 2: 종료됨${NC}"
            fi
            
            # Agent 3 상태 확인
            if ps -p $AGENT3_PID > /dev/null 2>&1; then
                echo -e "${GREEN}✅ Agent 3: 실행 중${NC}"
            else
                echo -e "${RED}❌ Agent 3: 종료됨${NC}"
            fi
            
            echo "----------------------------------------"
            sleep 30
        done
    }
    
    # 모니터링 시작 (백그라운드)
    monitor_agents &
    MONITOR_PID=$!
    
    echo -e "${YELLOW}📊 Agent 모니터링 시작 (PID: $MONITOR_PID)${NC}"
    echo -e "${YELLOW}🛑 모든 Agent를 중지하려면 Ctrl+C를 누르세요${NC}"
    
    # 사용자 입력 대기
    read -p "Enter를 눌러 계속하거나 Ctrl+C로 중지하세요..."
    
    # Agent 중지
    echo -e "${YELLOW}🛑 Agent 중지 중...${NC}"
    kill $AGENT1_PID $AGENT2_PID $AGENT3_PID $MONITOR_PID 2>/dev/null
    echo -e "${GREEN}✅ 모든 Agent 중지 완료${NC}"
}

# Agent 실행 옵션
echo -e "${YELLOW}선택하세요:${NC}"
echo "1. Agent 1만 실행 (Core Infrastructure)"
echo "2. Agent 2만 실행 (Social Features)"
echo "3. Agent 3만 실행 (Advanced Features)"
echo "4. 모든 Agent 동시 실행"
echo "5. Agent 상태 확인"
echo "6. 종료"

read -p "선택 (1-6): " choice

case $choice in
    1)
        echo -e "${BLUE}🤖 Agent 1: Core Infrastructure Agent 실행${NC}"
        ./agent1-work.sh
        ;;
    2)
        echo -e "${BLUE}🤖 Agent 2: Social Features Agent 실행${NC}"
        ./agent2-work.sh
        ;;
    3)
        echo -e "${BLUE}🤖 Agent 3: Advanced Features Agent 실행${NC}"
        ./agent3-work.sh
        ;;
    4)
        run_agents_parallel
        ;;
    5)
        echo -e "${YELLOW}📊 Agent 상태 확인${NC}"
        ps aux | grep -E "(agent1|agent2|agent3)" | grep -v grep
        ;;
    6)
        echo -e "${GREEN}👋 종료${NC}"
        exit 0
        ;;
    *)
        echo -e "${RED}❌ 잘못된 선택입니다${NC}"
        exit 1
        ;;
esac

echo -e "${GREEN}🎉 SpiritScribe Backend Agent 실행 완료${NC}"
