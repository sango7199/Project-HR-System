// 전역변수
selectedEmployees = {};
//========================================결재 상태 변경 =================================================================

// 결재 상태 변경 시 "거절"을 선택한 경우 거절 사유를 적는 칸이 생김
function toggleRejectionReason(status, rejectReason) {

    const selectElement = document.getElementById(status);
    const rejectionReasonContainer = document.getElementById(rejectReason);


    // 요소가 존재하지 않으면 오류 메시지 출력
    if (!selectElement || !rejectionReasonContainer) {
        console.error("요소를 찾을 수 없습니다. 사용된 ID를 확인하세요.");
        return;
    }

    // "거절"을 선택했을 때 거절 사유 입력란 표시
    if (selectElement.value === "거절") {
        rejectionReasonContainer.style.display = "block";
        rejectionReasonContainer.querySelector('textarea').setAttribute("required", "required");
    } else {
        rejectionReasonContainer.style.display = "none";
        rejectionReasonContainer.querySelector('textarea').removeAttribute("required");
    }
}

//
function toggleApprovalSection(formId) {
    var approvalForm = document.getElementById(formId);
    approvalForm.style.display = approvalForm.style.display === 'none' ? 'block' : 'none';
}


//========================================결재 상태 변경 =================================================================
//========================================== 임원 선택 ==================================================================

// 부서 선택 및 전체 선택 이벤트 리스너 초기화
function initEventListeners() {
    const departmentSelect = document.getElementById('departmentId');
    const selectAllButton = document.getElementById('selectAllEmployeesButton'); // 전체 선택 버튼
    const deselectAllButton = document.getElementById('deselectAllEmployeesButton'); // 전체 해제 버튼

    departmentSelect.addEventListener('change', loadEmployeesByDepartment);
    if (selectAllButton) {
        selectAllButton.addEventListener('click', selectAllEmployees); // 전체 선택 이벤트 리스너
    }
    if (deselectAllButton) {
        deselectAllButton.addEventListener('click', deselectAllEmployees); // 전체 해제 이벤트 리스너
    }
}

// 선택된 부서에 맞는 임원들 리스트를 반환해줌
function loadEmployeesByDepartment() {
    const departmentId = document.getElementById('departmentId').value;

    // 부서 선택 시 employeeSection을 보여줌
    const employeeSection = document.getElementById('employeeSection');
    if (departmentId) {
        employeeSection.style.display = 'block';
    } else {
        employeeSection.style.display = 'none';
    }

    fetch(`/api/employee/department/list/` + departmentId)
        .then(response => {
            if (!response.ok) {
                throw new Error('response 오류!');
            }
            return response.json();
        })
        .then(employeeList => {
            const availableContainer = document.getElementById('idContainer');
            availableContainer.innerHTML = '';

            employeeList.forEach(employee => {
                if (!selectedEmployees[employee.employeeId]) {
                    const employeeItem = createEmployeeItem(employee.employeeId, employee.name, false);
                    availableContainer.appendChild(employeeItem);
                }
            });
        })
        .catch(error => console.error('데이터를 읽어 올 수 없음', error));
}

// 임원 리스트 아이템 생성
function createEmployeeItem(employeeId, employeeName, isSelected) {
    const div = document.createElement('div');
    div.className = 'employee-item';
    div.textContent = employeeName;

    const actionButton = document.createElement('button');
    actionButton.dataset.id = employeeId; // 버튼에 data-id 속성을 추가
    if (isSelected) {
        actionButton.textContent = '해제';
        actionButton.addEventListener('click', () => {
            delete selectedEmployees[employeeId];
            updateSelectedEmployees();
        });
    } else {
        actionButton.textContent = '선택';
        actionButton.addEventListener('click', () => {
            selectedEmployees[employeeId] = employeeName;
            updateSelectedEmployees();
        });
    }

    div.appendChild(actionButton);
    return div;
}

// 선택된 임원들을 나열함
function updateSelectedEmployees() {
    const selectedEmployeesContainer = document.getElementById('selectedEmployees');
    const availableContainer = document.getElementById('idContainer');

    selectedEmployeesContainer.innerHTML = '';
    availableContainer.innerHTML = '';

    // 선택된 임원 목록을 먼저 갱신
    Object.keys(selectedEmployees).forEach(id => {
        const employeeItem = createEmployeeItem(id, selectedEmployees[id], true);
        selectedEmployeesContainer.appendChild(employeeItem);
    });

    // 임원 리스트를 갱신하여 중복 없이 남은 임원만 다시 나열
    loadEmployeesByDepartment();
}

// 전체 선택 기능
function selectAllEmployees() {
    const employeeItems = document.querySelectorAll('#idContainer .employee-item'); // 임원 목록을 가져옴

    employeeItems.forEach(item => {
        const button = item.querySelector('button');
        const employeeId = button.dataset.id; // 버튼에 설정된 data-id 속성값을 가져옴
        const employeeName = item.textContent.replace('선택', '').trim();

        // 이미 선택된 임원이 아니라면 선택된 목록에 추가
        if (!selectedEmployees[employeeId]) {
            selectedEmployees[employeeId] = employeeName;
        }
    });

    updateSelectedEmployees();
}

// 전체 해제
function deselectAllEmployees() {
    selectedEmployees = {}; // 선택된 임원들 초기화
    updateSelectedEmployees();
}

//=================================================== 임원 선택 =========================================================
//============================================= formData 유효성 검사 ====================================================
function validateReportForm(event) {

    event.preventDefault();

    let nameList;
    const title = document.getElementById("title").value.trim();
    const content = document.getElementById("content").value.trim();
    // 선택된 결재자를 nameList로 정의
    if (Object.keys(selectedEmployees).length > 0) {
        nameList = Object.values(selectedEmployees);
    } else {
    // 이미 결재자가 있다면 nameList에 정의 (보고서 수정 시, 요청에 의한 보고서 작성 시)
        nameList = document.getElementById('currentApproverName').value;
    }

    const completeDate = document.getElementById("completeDate").value;
    const errorAlert = document.getElementById("error-alert");

    if (errorAlert) {
        errorAlert.textContent = "";
    } else {
        console.error("Error message element not found.");
    }

    if (title === "") {
        errorAlert.textContent = "제목을 입력해주세요.";
        return false;
    }
    if (content === "") {
        errorAlert.textContent = "내용을 입력해주세요.";
        return false;
    }
    if (nameList.length === 0 && document.getElementById('currentApproverName') === null) {
            errorAlert.textContent = "결재자를 선택해주세요.";
            return false;
    }
    if (completeDate === "") {
        errorAlert.textContent = "업무 완료 날짜를 입력해주세요";
        return false;
    }
    return true;
}

function validateRequestForm(event) {
    event.preventDefault();

    const requestNote = document.getElementById("requestNote").value.trim();
    // 선택된 결재자를 nameList로 정의
    let nameList;
    if (Object.keys(selectedEmployees).length > 0) {
        nameList = Object.values(selectedEmployees);
    } else {
        // 이미 결재자가 있다면 nameList에 정의 (보고서 수정 시, 요청에 의한 보고서 작성 시)
        nameList = document.getElementById('currentApproverName').value;
    }
    const dueDate = document.getElementById("dueDate").value;
    const errorAlert = document.getElementById("error-alert");

    if (errorAlert) {
        errorAlert.textContent = "";
    } else {
        console.error("Error message element not found.");
    }

    if (nameList.length === 0) {
        errorAlert.textContent = "결재자를 선택해주세요.";
        return false;
    }
    if (dueDate === "") {
        errorAlert.textContent = "마감일자를 입력해주세요.";
        return false;
    }
    if (requestNote === "") {
        errorAlert.textContent = "요청사항을 입력해주세요.";
        return false;
    }
    return true;
}

// form 제출 처리
function handleReportForm(event) {
    event.preventDefault();

    const form = event.target.closest('form');
    const actionUrl = document.getElementById('form').action;

    return { form, actionUrl };
}
//============================================= formData 유효성 검사 =====================================================
//=============================================== formData 전송 =========================================================
// 보고서 작성 시
function submitReportForm(event) {
    // 유효성 검사 실행
    if (!validateReportForm(event)) { return; }

    // form 제출 처리
    const { form, actionUrl } = handleReportForm(event);
    const formData = new FormData();

    // FormData 객체에 report 필드를 추가
    const report = {
        title: form.title.value,
        content: form.content.value,
        idList: Object.keys(selectedEmployees),
        nameList: Object.values(selectedEmployees),
        completeDate: form.completeDate.value
    };
    formData.append("report", new Blob([JSON.stringify(report)], { type: "application/json" }));

    // filesArr에 저장된 파일들을 FormData에 추가
    if (filesArr != null) {
        filesArr.forEach((file, index) => {
            if (!file.is_delete) { // 삭제된 파일 제외
                formData.append('reportFiles', file);
            }
        });
    }

    // 데이터를 서버로 전송
    fetch(actionUrl, {
        method: 'POST',
        body: formData
    })
        .then(response => response.text().then(data => ({
            status: response.status,
            text: data
        })))
        .then(response => {
            console.log('서버 응답 데이터 :', response.text);
            if (response.status === 200) {
                alert(response.text); // 성공 메시지 알림
                window.location.href = '/report/list';
            } else if (response.status === 404) {
                alert(response.text); // 404 오류 메세지 알림
                window.location.reload();
            } else if (response.status === 400) {
                alert(response.text); // 400 오류 메시지 알림
            } else if (response.status === 500) {
                alert(response.text); // 500 오류 메시지 알림
            } else {
                alert('보고서 작성 중 오류가 발생하였습니다.\n재등록 시도 후 여전히 문제가 발생하면 관리자에게 문의해주세요');
                window.location.reload();
            }
        })
        .catch(error => {
            console.error('Error :', error.message);
            alert('오류가 발생하였습니다.\n관리자에게 문의해주세요.');
        });
}


// 보고서 수정 시
function updateReportForm(event) {
    // 유효성 검사 실행
    if (!validateReportForm(event)) { return; }

    // form 제출 처리
    const { form, actionUrl } = handleReportForm(event);
    const formData = new FormData();

    // FormData 객체에 report 필드를 추가
    const idList = Object.keys(selectedEmployees).length === 0
        ? [document.getElementById('currentApproverId').value]
        : Object.keys(selectedEmployees);

    const nameList = Object.keys(selectedEmployees).length === 0
        ? [document.getElementById('currentApproverName').value]
        : Object.values(selectedEmployees);

    const report = {
        reportId: document.getElementById('reportId').value,
        writerId: document.getElementById('writerId').value,
        title: document.getElementById('title').value,
        content: document.getElementById('content').value,
        idList: idList,
        nameList: nameList,
        completeDate: document.getElementById('completeDate').value
    };
    formData.append("report", new Blob([JSON.stringify(report)], { type: "application/json" }));

    // filesArr에 저장된 파일들을 FormData에 추가
    filesArr.forEach((file) => {
        if (!file.is_delete) { // 삭제된 파일 제외
            if (file.id) {
                // 기존 파일의 경우
                formData.append('registeredFileIdList', file.id);
            } else {
                // 새로 추가된 파일의 경우
                formData.append('reportFiles', file);
            }
        }
    });

    // 사용자가 파일 수정 버튼을 누르지 않은 경우, 기존 파일 전송
    if (filesArr.length === 0 && registeredFileIdList && registeredFileIdList.length > 0) {
        registeredFileIdList.forEach((fileId, index) => {
            formData.append('registeredFileIdList', fileId);
        });
    }

    // 데이터를 서버로 전송
    fetch(actionUrl, {
        method: 'POST',
        body: formData
    })
        .then(response => response.text().then(data => ({
            status: response.status,
            text: data
        })))
        .then(response => {
            console.log('서버 응답 데이터 :', response.text);
            if (response.status === 200) {
                alert(response.text); // 성공 메시지 알림
                window.location.href = '/report/list';
            } else if (response.status === 404) {
                alert(response.text); // 404 오류 메세지 알림
                window.location.reload();
            } else if (response.status === 400) {
                alert(response.text); // 400 오류 메시지 알림
            } else if (response.status === 500) {
                alert(response.text); // 500 오류 메시지 알림
            } else {
                alert('보고서 수정 중 오류가 발생하였습니다.\n재등록 시도 후 여전히 문제가 발생하면 관리자에게 문의해주세요');
                window.location.reload();
            }
        })
        .catch(error => {
            console.error('Error :', error.message);
            alert('오류가 발생하였습니다.\n관리자에게 문의해주세요.');
        });
}
// 보고서 수정 시 - 기존 파일 + 업로드 파일 관리
function deleteReportFile(num) {
    // 'filesArr' 배열에서 해당 파일 객체의 'is_delete' 속성 설정
    filesArr[num].is_delete = true;

    // 파일 ID가 있으면 registeredFileIdList에서 제거
    if (filesArr[num].id) {
        const index = registeredFileIdList.indexOf(filesArr[num].id);
        if (index > -1) {
            registeredFileIdList.splice(index, 1);
        }
    }

    // UI에서 파일을 삭제
    document.querySelector("#file" + num).remove();
}

// 요청 작성 시
function submitRequestForm(event) {
    // 유효성 검사 실행
    if (!validateRequestForm(event)) { return; }

    // form 제출 처리
    const { form, actionUrl } = handleReportForm(event);

    // FormData 객체에 request 필드를 추가
    const request = {
        requestNote: form.requestNote.value,
        idList: Object.keys(selectedEmployees),
        nameList: Object.values(selectedEmployees),
        dueDate: form.dueDate.value
    };

    // 데이터를 서버로 전송
    fetch(actionUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(request)
    })
        .then(response => response.text().then(data => ({
            status: response.status,
            text: data
        })))
        .then(response => {
            console.log('서버 응답 데이터 :', response.text);
            if (response.status === 200) {
                alert(response.text); // 성공 메시지 알림
                window.location.href = '/admin/request/requestList';
            } else if (response.status === 404) {
                alert(response.text); // 404 오류 메세지 알림
                window.location.reload();
            } else if (response.status === 400) {
                alert(response.text); // 400 오류 메시지 알림
            } else if (response.status === 500) {
                alert(response.text); // 500 오류 메시지 알림
            } else {
                alert('요청 작성 중 오류가 발생하였습니다.\n재등록 시도 후 여전히 문제가 발생하면 관리자에게 문의해주세요');
                window.location.reload();
            }
        })
        .catch(error => {
            console.error('Error :', error.message);
            alert('오류가 발생하였습니다.\n관리자에게 문의해주세요.');
        });
}

// 요청 수정 시
function updateRequestForm(event) {
    // 유효성 검사 실행
    if (!validateRequestForm(event)) { return; }

    // form 제출 처리
    const { form, actionUrl } = handleReportForm(event);

    // FormData 객체에 report 필드를 추가
    const idList = Object.keys(selectedEmployees).length === 0
        ? [document.getElementById('currentApproverId').value]
        : Object.keys(selectedEmployees);

    const nameList = Object.keys(selectedEmployees).length === 0
        ? [document.getElementById('currentApproverName').value]
        : Object.values(selectedEmployees);


    const request = {
        requestId: form.requestId.value,
        requesterId: form.requesterId.value,
        requestNote: form.requestNote.value,
        idList: idList,
        nameList: nameList,
        dueDate: form.dueDate.value
    };

    // 데이터를 서버로 전송
    fetch(actionUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(request)
    })
        .then(response => response.text().then(data => ({
            status: response.status,
            text: data
        })))
        .then(response => {
            console.log('서버 응답 데이터 :', response.text);
            if (response.status === 200) {
                alert(response.text); // 성공 메시지 알림
                window.location.href = '/admin/request/requestList';
            } else if (response.status === 404) {
                alert(response.text); // 404 오류 메세지 알림
                window.location.reload();
            } else if (response.status === 400) {
                alert(response.text); // 400 오류 메시지 알림
            } else if (response.status === 500) {
                alert(response.text); // 500 오류 메시지 알림
            } else {
                alert('요청 작성 중 오류가 발생하였습니다.\n재등록 시도 후 여전히 문제가 발생하면 관리자에게 문의해주세요');
                window.location.reload();
            }
        })
        .catch(error => {
            console.error('Error :', error.message);
            alert('오류가 발생하였습니다.\n관리자에게 문의해주세요.');
        });
}

// 내가 작성한 보고서(STAFF), 결재할 보고서(MANAGER) - 검색 시
function submitReportSearchForm(event) {
    event.preventDefault();  // 기본 폼 제출 방지

    const form = event.target.closest('form'); // 이벤트가 발생한 폼 요소를 가져옴
    const actionUrl = form.action; // 폼의 action 속성을 가져옴

    const searchDateOption = document.getElementById('searchDate').value;
    const dateRange = getDateRangeByOption(searchDateOption);
    const approvalStatus = document.getElementById('approvalStatus').value;
    const searchType = document.getElementById('searchType').value;
    const keyword = document.getElementById('keywordField').value;
    const size = document.getElementById('size').value;

    let startDate = dateRange.startDate;
    let endDate = dateRange.endDate;

    if (searchDateOption === 'custom') {
        startDate = document.getElementById('startDate').value;
        endDate = document.getElementById('endDate').value;
    }

    // URLSearchParams 객체 생성
    const params = new URLSearchParams({
        searchDate: searchDateOption,
        approvalStatus: approvalStatus,
        searchType: searchType,
        keyword: keyword,
        size: size
    });

    if (startDate && endDate) {
        params.append('startDate', startDate);
        params.append('endDate', endDate);
    }

    // 데이터를 서버로 GET 요청 전송
    window.location.href = `${actionUrl}?${params.toString()}`;
}

// 내게 온 요청(STAFF), 내가 쓴 요청(MANAGER) - 검색 시
function submitRequestSearchForm(event) {
    event.preventDefault();  // 기본 폼 제출 방지

    const form = event.target.closest('form'); // 이벤트가 발생한 폼 요소를 가져옴
    const actionUrl = form.action; // 폼의 action 속성을 가져옴

    const searchDateOption = document.getElementById('searchDate').value;
    const dateRange = getDateRangeByOption(searchDateOption);
    const searchType = document.getElementById('searchType').value;
    const keyword = document.getElementById('keywordField').value;
    const size = document.getElementById('size').value;

    let startDate = dateRange.startDate;
    let endDate = dateRange.endDate;

    if (searchDateOption === 'custom') {
        startDate = document.getElementById('startDate').value;
        endDate = document.getElementById('endDate').value;
    }

    // URLSearchParams 객체 생성
    const params = new URLSearchParams({
        searchDate: searchDateOption,
        searchType: searchType,
        keyword: keyword,
        size: size
    });

    if (startDate && endDate) {
        params.append('startDate', startDate);
        params.append('endDate', endDate);
    }

    // 데이터를 서버로 GET 요청 전송
    window.location.href = `${actionUrl}?${params.toString()}`;
}

// 내가 쓴 보고서 통계(STAFF)
function submitReportStatisticForm(event) {
    event.preventDefault();  // 기본 폼 제출 방지

    const form = event.target.closest('form'); // 이벤트가 발생한 폼 요소를 가져옴
    const actionUrl = form.action; // 폼의 action 속성을 가져옴

    const searchDateOption = document.getElementById('searchDate').value;
    const dateRange = getDateRangeByOption(searchDateOption);

    let startDate = dateRange.startDate;
    let endDate = dateRange.endDate;

    const params = new URLSearchParams({
    });

    if (searchDateOption === 'custom') {
        startDate = document.getElementById('startDate').value;
        endDate = document.getElementById('endDate').value;
    }

    if (startDate && endDate) {
        params.append('startDate', startDate);
        params.append('endDate', endDate);
    }

    // 데이터를 서버로 GET 요청 전송
    window.location.href = `${actionUrl}?${params.toString()}`;
}

// SATFF들의 보고서 통계 조회 (MANAGER)
function submitReportStatisticFormByManager(event) {
    event.preventDefault();  // 기본 폼 제출 방지

    const form = event.target.closest('form'); // 이벤트가 발생한 폼 요소를 가져옴
    const actionUrl = form.action; // 폼의 action 속성을 가져옴

    const searchDateOption = document.getElementById('searchDate').value;
    const dateRange = getDateRangeByOption(searchDateOption);

    let startDate = dateRange.startDate;
    let endDate = dateRange.endDate;

    const params = new URLSearchParams({
        idList: Object.keys(selectedEmployees),
        nameList: Object.values(selectedEmployees),
    });

    if (searchDateOption === 'custom') {
        startDate = document.getElementById('startDate').value;
        endDate = document.getElementById('endDate').value;
    }

    if (startDate && endDate) {
        params.append('startDate', startDate);
        params.append('endDate', endDate);
    }

    params.forEach((value, key) => {
        console.log(key, value);
    })

    // 데이터를 서버로 GET 요청 전송
    window.location.href = `${actionUrl}?${params.toString()}`;
}



//=============================================== formData 전송 =========================================================
//============================================= 검색 관련 메소드 =========================================================
// 선택된 기간에 따라 날짜 설정
function getDateRangeByOption(option) {
    const today = new Date();
    let startDate;
    let endDate = today;

    switch(option) {
        case 'week':
            startDate = new Date();
            startDate.setDate(today.getDate() - 7);
            break;
        case 'month':
            startDate = new Date();
            startDate.setMonth(today.getMonth() - 1);
            break;
        case 'sixMonth':
            startDate = new Date();
            startDate.setMonth(today.getMonth() - 6);
            break;
        case 'year':
            startDate = new Date();
            startDate.setFullYear(today.getFullYear() - 1);
            break;
        case 'null': // 전체기간
        default:
            startDate = null;
            endDate = null;
            break;
    }

    return {
        startDate: startDate ? formatDate(startDate) : null,
        endDate: endDate ? formatDate(endDate) : null
    };
}

// 날짜 형태에 맞게 변환
function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

// 사용자가 직접 날짜입력 시 실행되는 메소드
function toggleCustomDateFields() {
    const searchDate = document.getElementById('searchDate').value;
    const customDateRange = document.getElementById('customDateRange');

    if (searchDate === 'custom') {
        customDateRange.style.display = 'block';
    } else {
        customDateRange.style.display = 'none';
    }
}


//============================================= 검색 관련 메소드 =========================================================
//========================================== 통계 관련 메소드 ============================================================

// 통계 - 선택된 임원 목록 중 임원을 삭제하는 메소드
function removeWriter(button) {

    // 선택된 임원 목록에서 해당 임원 삭제
    var employeeId = button.getAttribute('data-employee-id');

    // 부모 노드를 찾아서 삭제
    var parentLi = button.parentNode;
    parentLi.parentNode.removeChild(parentLi);

    // 선택된 임원 ID 목록 갱신
    const remainingIds = [];
    document.querySelectorAll('#selected-writers-list li button').forEach(button => {
        remainingIds.push(button.getAttribute('data-employee-id'));
    });

    // 선택된 임원이 모두 제거된 경우 "모든 임원" 항목을 추가
    if (remainingIds.length === 0) {
        var ul = document.getElementById('selected-writers-list');
        var allMembersLi = document.createElement("li");
        allMembersLi.innerHTML = "<span>모든 임원</span>";
        ul.appendChild(allMembersLi);
    }

    // 서버에 갱신된 임원 목록을 전송하여 통계 데이터를 갱신
    updateChartWithSelectedWriters(remainingIds);
}

// main.html에 통계를 다시 갱신하는 매소드(임원 삭제 후)
function updateChartWithSelectedWriters(ids) {
    const urlParams = new URLSearchParams(window.location.search);
    const startDate = urlParams.get('startDate') || null;
    const endDate = urlParams.get('endDate') || null;

    const payload = {
        idList: ids,
        startDate: startDate,
        endDate: endDate
    };

    $.ajax({
        url: '/admin/request/updateStats',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(payload),
        success: function (response) {
            // 갱신된 통계 데이터를 이용하여 차트를 다시 그리기
            var stats = JSON.parse(response.statsJson);
            var data = google.visualization.arrayToDataTable(stats);
            var options = {};

            var chart = new google.charts.Bar(document.getElementById('columnchart_material'));
            chart.draw(data, google.charts.Bar.convertOptions(options));
        },
        error: function (error) {
            console.error('Error:', error);
        }
    });
}
//========================================== 통계 관련 메소드 =================================================================
//========================================== 날짜 설정 메소드 =================================================================
function validateDateRange() {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (startDate && endDate && startDate > endDate) {
        alert('시작 월은 종료 월보다 늦을 수 없습니다.');
        return false; // 폼 제출을 막음
    }

    return true; // 폼 제출 허용
}
//========================================== 날짜 설정 메소드 =================================================================