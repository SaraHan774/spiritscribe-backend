#!/bin/bash

# SpiritScribe Backend - 모든 Agent 동시 실행 스크립트
echo "🚀 SpiritScribe Backend - 모든 Agent 동시 실행"
echo "=================================================="

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 프로젝트 루트 디렉토리
PROJECT_ROOT="/Users/gahee/spiritscribe-backend"

# Agent별 워크스페이스 디렉토리 생성
mkdir -p "$PROJECT_ROOT/agent1-core"
mkdir -p "$PROJECT_ROOT/agent2-social"
mkdir -p "$PROJECT_ROOT/agent3-advanced"

echo -e "${GREEN}✅ Agent 워크스페이스 디렉토리 생성 완료${NC}"

# Agent별 작업 스크립트 실행
echo -e "${YELLOW}🚀 모든 Agent 동시 실행 시작...${NC}"

# Agent 1 실행 (백그라운드)
echo -e "${BLUE}🤖 Agent 1: Core Infrastructure Agent 시작${NC}"
./agent1-work.sh > agent1.log 2>&1 &
AGENT1_PID=$!

# Agent 2 실행 (백그라운드)
echo -e "${BLUE}🤖 Agent 2: Social Features Agent 시작${NC}"
./agent2-work.sh > agent2.log 2>&1 &
AGENT2_PID=$!

# Agent 3 실행 (백그라운드)
echo -e "${BLUE}🤖 Agent 3: Advanced Features Agent 시작${NC}"
./agent3-work.sh > agent3.log 2>&1 &
AGENT3_PID=$!

echo -e "${GREEN}✅ 모든 Agent가 백그라운드에서 실행 중${NC}"
echo -e "${YELLOW}📊 Agent 상태 모니터링:${NC}"
echo "Agent 1 PID: $AGENT1_PID"
echo "Agent 2 PID: $AGENT2_PID"
echo "Agent 3 PID: $AGENT3_PID"

# Agent 상태 모니터링 함수
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

# 로그 파일 확인
echo -e "${YELLOW}📄 Agent 로그 파일:${NC}"
echo "Agent 1 로그: agent1.log"
echo "Agent 2 로그: agent2.log"
echo "Agent 3 로그: agent3.log"

echo -e "${GREEN}🎉 SpiritScribe Backend Agent 실행 완료${NC}"
