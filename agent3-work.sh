#!/bin/bash

# Agent 3: Advanced Features Agent 작업 스크립트
echo -e "\033[0;34m🤖 Agent 3: Advanced Features Agent 시작\033[0m"
echo "=================================================="

# 워크스페이스로 이동
cd "/Users/gahee/spiritscribe-backend/agent3-advanced"

# Cursor CLI로 Agent 실행
cursor --agent-config="/Users/gahee/spiritscribe-backend/.cursor/agent3-config.json" --workspace="/Users/gahee/spiritscribe-backend/agent3-advanced" --auto-start

echo -e "\033[0;32m✅ Agent 3 작업 완료\033[0m"
