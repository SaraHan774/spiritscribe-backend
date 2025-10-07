// SpiritScribe Admin JavaScript

$(document).ready(function() {
    // Initialize tooltips
    $('[data-bs-toggle="tooltip"]').tooltip();
    
    // Initialize popovers
    $('[data-bs-toggle="popover"]').popover();
    
    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        $('.alert').fadeOut('slow');
    }, 5000);
    
    // Refresh stats every 30 seconds
    setInterval(refreshStats, 30000);
    
    // Initial stats load
    refreshStats();
});

/**
 * 통계 새로고침
 */
function refreshStats() {
    $.ajax({
        url: '/v1/admin/stats',
        method: 'GET',
        success: function(data) {
            updateStatsDisplay(data);
        },
        error: function(xhr, status, error) {
            console.error('통계 조회 실패:', error);
        }
    });
}

/**
 * 통계 표시 업데이트
 */
function updateStatsDisplay(stats) {
    $('#currentUsers').text(stats.userCount || 0);
    $('#currentCheckins').text(stats.totalCheckIns || 0);
    $('#currentWhiskies').text(stats.totalWhiskies || 0);
    $('#systemStatus').text(stats.systemStatus || '알 수 없음');
}

/**
 * 성공 메시지 표시
 */
function showSuccess(message) {
    showAlert('success', message);
}

/**
 * 오류 메시지 표시
 */
function showError(message) {
    showAlert('danger', message);
}

/**
 * 정보 메시지 표시
 */
function showInfo(message) {
    showAlert('info', message);
}

/**
 * 경고 메시지 표시
 */
function showWarning(message) {
    showAlert('warning', message);
}

/**
 * 알림 표시
 */
function showAlert(type, message) {
    const alertHtml = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            <i class="fas fa-${getAlertIcon(type)}"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    $('#alertContainer').html(alertHtml);
    
    // Auto-hide after 5 seconds
    setTimeout(function() {
        $('.alert').fadeOut('slow');
    }, 5000);
}

/**
 * 알림 아이콘 가져오기
 */
function getAlertIcon(type) {
    const icons = {
        'success': 'check-circle',
        'danger': 'exclamation-triangle',
        'info': 'info-circle',
        'warning': 'exclamation-circle'
    };
    return icons[type] || 'info-circle';
}

/**
 * 로딩 상태 표시
 */
function showLoading(button) {
    const originalText = button.html();
    button.html('<span class="spinner-border spinner-border-sm" role="status"></span> 처리 중...');
    button.prop('disabled', true);
    
    return function() {
        button.html(originalText);
        button.prop('disabled', false);
    };
}

/**
 * API 호출
 */
function callApi(url, method, data, successCallback, errorCallback) {
    $.ajax({
        url: url,
        method: method,
        data: data,
        success: function(response) {
            if (successCallback) successCallback(response);
        },
        error: function(xhr, status, error) {
            console.error('API 호출 실패:', error);
            if (errorCallback) {
                errorCallback(xhr, status, error);
            } else {
                showError('요청 처리 중 오류가 발생했습니다.');
            }
        }
    });
}

/**
 * 폼 데이터 수집
 */
function getFormData(formId) {
    const form = $(`#${formId}`);
    const data = {};
    
    form.find('input, select, textarea').each(function() {
        const field = $(this);
        const name = field.attr('name') || field.attr('id');
        const value = field.val();
        
        if (name && value !== '') {
            data[name] = value;
        }
    });
    
    return data;
}

/**
 * 폼 초기화
 */
function resetForm(formId) {
    $(`#${formId}`)[0].reset();
}

/**
 * 페이지 새로고침
 */
function refreshPage() {
    location.reload();
}

/**
 * 외부 링크 열기
 */
function openExternal(url) {
    window.open(url, '_blank');
}

/**
 * 로그아웃 확인
 */
function confirmLogout() {
    if (confirm('정말 로그아웃하시겠습니까?')) {
        window.location.href = '/admin/logout';
    }
}

/**
 * 데이터 삭제 확인
 */
function confirmDelete(message) {
    return confirm(message || '정말 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.');
}

/**
 * 숫자 포맷팅
 */
function formatNumber(num) {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

/**
 * 날짜 포맷팅
 */
function formatDate(date) {
    return new Date(date).toLocaleDateString('ko-KR');
}

/**
 * 시간 포맷팅
 */
function formatTime(date) {
    return new Date(date).toLocaleTimeString('ko-KR');
}

/**
 * 날짜/시간 포맷팅
 */
function formatDateTime(date) {
    return new Date(date).toLocaleString('ko-KR');
}
