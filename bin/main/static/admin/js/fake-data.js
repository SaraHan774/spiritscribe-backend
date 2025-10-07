// Fake Data Generation JavaScript

$(document).ready(function() {
    // Form submissions
    $('#usersForm').on('submit', function(e) {
        e.preventDefault();
        generateFakeUsers();
    });
    
    $('#checkinsForm').on('submit', function(e) {
        e.preventDefault();
        generateFakeCheckIns();
    });
    
    $('#whiskiesForm').on('submit', function(e) {
        e.preventDefault();
        generateFakeWhiskies();
    });
    
    $('#allDataForm').on('submit', function(e) {
        e.preventDefault();
        generateAllFakeData();
    });
    
    // Button clicks
    $('#resetDataBtn').on('click', function() {
        resetAllData();
    });
    
    $('#refreshStatsBtn').on('click', function() {
        refreshStats();
    });
    
    // Initial stats load
    refreshStats();
});

/**
 * 가짜 사용자 생성
 */
function generateFakeUsers() {
    const count = parseInt($('#userCount').val());
    
    if (count < 1 || count > 1000) {
        showError('사용자 수는 1-1000 사이여야 합니다.');
        return;
    }
    
    const button = $('#usersForm button[type="submit"]');
    const hideLoading = showLoading(button);
    
    callApi('/v1/admin/fake-data/users', 'POST', { count: count }, function(response) {
        hideLoading();
        showSuccess(response.message);
        refreshStats();
    }, function(xhr, status, error) {
        hideLoading();
        const errorMessage = xhr.responseJSON?.error || '사용자 생성 중 오류가 발생했습니다.';
        showError(errorMessage);
    });
}

/**
 * 가짜 체크인 생성
 */
function generateFakeCheckIns() {
    const count = parseInt($('#checkinCount').val());
    
    if (count < 1 || count > 1000) {
        showError('체크인 수는 1-1000 사이여야 합니다.');
        return;
    }
    
    const button = $('#checkinsForm button[type="submit"]');
    const hideLoading = showLoading(button);
    
    callApi('/v1/admin/fake-data/checkins', 'POST', { count: count }, function(response) {
        hideLoading();
        showSuccess(response.message);
        refreshStats();
    }, function(xhr, status, error) {
        hideLoading();
        const errorMessage = xhr.responseJSON?.error || '체크인 생성 중 오류가 발생했습니다.';
        showError(errorMessage);
    });
}

/**
 * 가짜 위스키 생성
 */
function generateFakeWhiskies() {
    const count = parseInt($('#whiskeyCount').val());
    
    if (count < 1 || count > 1000) {
        showError('위스키 수는 1-1000 사이여야 합니다.');
        return;
    }
    
    const button = $('#whiskiesForm button[type="submit"]');
    const hideLoading = showLoading(button);
    
    callApi('/v1/admin/fake-data/whiskies', 'POST', { count: count }, function(response) {
        hideLoading();
        showSuccess(response.message);
        refreshStats();
    }, function(xhr, status, error) {
        hideLoading();
        const errorMessage = xhr.responseJSON?.error || '위스키 생성 중 오류가 발생했습니다.';
        showError(errorMessage);
    });
}

/**
 * 종합 가짜 데이터 생성
 */
function generateAllFakeData() {
    const userCount = parseInt($('#allUserCount').val());
    const checkinCount = parseInt($('#allCheckinCount').val());
    const whiskeyCount = parseInt($('#allWhiskeyCount').val());
    
    if (userCount < 0 || userCount > 1000 || 
        checkinCount < 0 || checkinCount > 1000 || 
        whiskeyCount < 0 || whiskeyCount > 1000) {
        showError('모든 수는 0-1000 사이여야 합니다.');
        return;
    }
    
    const button = $('#allDataForm button[type="submit"]');
    const hideLoading = showLoading(button);
    
    callApi('/v1/admin/fake-data/all', 'POST', { 
        userCount: userCount, 
        checkInCount: checkinCount, 
        whiskeyCount: whiskeyCount 
    }, function(response) {
        hideLoading();
        showSuccess(response.message);
        
        // 결과 상세 표시
        if (response.results) {
            let resultMessage = '생성된 데이터:\n';
            for (const [key, value] of Object.entries(response.results)) {
                resultMessage += `- ${key}: ${value}개\n`;
            }
            showInfo(resultMessage);
        }
        
        refreshStats();
    }, function(xhr, status, error) {
        hideLoading();
        const errorMessage = xhr.responseJSON?.error || '데이터 생성 중 오류가 발생했습니다.';
        showError(errorMessage);
    });
}

/**
 * 모든 데이터 초기화
 */
function resetAllData() {
    if (!confirmDelete('모든 가짜 데이터를 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
        return;
    }
    
    const button = $('#resetDataBtn');
    const hideLoading = showLoading(button);
    
    callApi('/v1/admin/fake-data', 'DELETE', {}, function(response) {
        hideLoading();
        showSuccess(response.message);
        refreshStats();
    }, function(xhr, status, error) {
        hideLoading();
        const errorMessage = xhr.responseJSON?.error || '데이터 초기화 중 오류가 발생했습니다.';
        showError(errorMessage);
    });
}

/**
 * 통계 새로고침
 */
function refreshStats() {
    callApi('/v1/admin/stats', 'GET', {}, function(data) {
        updateStatsDisplay(data);
    }, function(xhr, status, error) {
        console.error('통계 조회 실패:', error);
        showError('통계 조회 중 오류가 발생했습니다.');
    });
}

/**
 * 통계 표시 업데이트
 */
function updateStatsDisplay(stats) {
    $('#currentUsers').text(formatNumber(stats.userCount || 0));
    $('#currentCheckins').text(formatNumber(stats.totalCheckIns || 0));
    $('#currentWhiskies').text(formatNumber(stats.totalWhiskies || 0));
    $('#systemStatus').text(stats.systemStatus || '알 수 없음');
    
    // 시스템 상태에 따른 색상 변경
    const statusElement = $('#systemStatus');
    if (stats.systemStatus === '정상') {
        statusElement.removeClass('text-danger text-warning').addClass('text-success');
    } else if (stats.systemStatus === '경고') {
        statusElement.removeClass('text-success text-danger').addClass('text-warning');
    } else {
        statusElement.removeClass('text-success text-warning').addClass('text-danger');
    }
}

/**
 * 폼 유효성 검사
 */
function validateForm(formId) {
    const form = $(`#${formId}`);
    let isValid = true;
    
    form.find('input[required]').each(function() {
        const field = $(this);
        const value = field.val();
        
        if (!value || value.trim() === '') {
            field.addClass('is-invalid');
            isValid = false;
        } else {
            field.removeClass('is-invalid');
        }
    });
    
    return isValid;
}

/**
 * 폼 초기화
 */
function resetForm(formId) {
    $(`#${formId}`)[0].reset();
    $(`#${formId} input`).removeClass('is-invalid');
}

/**
 * 입력 필드 실시간 유효성 검사
 */
function setupRealTimeValidation() {
    $('input[required]').on('input', function() {
        const field = $(this);
        const value = field.val();
        
        if (value && value.trim() !== '') {
            field.removeClass('is-invalid').addClass('is-valid');
        } else {
            field.removeClass('is-valid').addClass('is-invalid');
        }
    });
}

// 실시간 유효성 검사 설정
$(document).ready(function() {
    setupRealTimeValidation();
});
