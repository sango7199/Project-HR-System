// 유효성 검사 - 사원 등록
function validateForm(event) {
    event.preventDefault();

    const pic = document.getElementById("picture").files;
    const name = document.getElementById("name").value.trim();
    const birth = document.getElementById("birth").value.trim();
    const residentRegistrationNumber = document.getElementById("residentRegistrationNumber").value.trim();
    const phoneInput = document.getElementById("phoneInput").value.trim();
    const emailLocal = document.getElementById("emailLocal").value.trim();
    const emailDomain = document.getElementById("emailDomain").value.trim();
    const address = document.getElementById("address").value.trim();
    const detailAddressInput = document.getElementById("detailAddressInput").value.trim();
    const maritalStatus = document.querySelector('input[name="maritalStatus"]:checked');
    const numDependents = document.getElementById("numDependents").value.trim();
    const numChildren = document.getElementById("numChildren").value.trim();
    const department = document.getElementById("department").value;
    const position = document.getElementById("position").value;
    const hireDate = document.getElementById("hireDate").value;

    let errorMessage = document.getElementById("error-message");

    if (errorMessage) {
        errorMessage.textContent = "";
    } else {
        console.error("에러 메세지 요소를 찾을 수 없습니다.");
    }

    // 유효성 검사 함수
    function showError(inputId, message, isBottomBorder = false) {
        const inputElement = document.getElementById(inputId);
        errorMessage.textContent = message;

        // 빨간 테두리와 흔들림 효과 추가
        if (isBottomBorder) {
            inputElement.classList.add("input-error-bottom", "shake");
        } else {
            inputElement.classList.add("input-error", "shake");
        }

        // 5초 후 빨간 테두리 제거
        setTimeout(() => {
            inputElement.classList.remove("input-error", "input-error-bottom");
        }, 5000);

        // 애니메이션이 끝난 후 흔들림 제거
        setTimeout(() => {
            inputElement.classList.remove("shake");
        }, 300);

        return false;
    }

    // 각 필드 유효성 검사
    if (pic.length === 0) {
        return showError("uploadArea", "사원 사진을 업로드해주세요");
    }
    if (name === "" || !validateName(name)) {
        return showError("name", "유효한 이름을 입력해주세요. (한글 또는 영어만 허용)", true);
    }
    if (!validateBirthDate(birth)) {
        return showError("birth", "유효한 생년월일(6자리 숫자)을 입력해주세요.", true);
    }
    if (residentRegistrationNumber === "" || residentRegistrationNumber.length !== 7 || !/^\d+$/.test(residentRegistrationNumber)) {
        return showError("residentRegistrationNumber", "유효한 주민번호 뒷자리(7자리 숫자)를 입력해주세요.", true);
    }
    if (phoneInput === "" || !/^\d{11}$/.test(phoneInput)) {
        return showError("phoneInput", "'-'를 제외한 유효한 전화번호를 입력해주세요.", true);
    }
    if (emailLocal === "") {
        return showError("emailLocal", "이메일을 입력해주세요.", true);
    }
    if (emailDomain === "") {
        return showError("emailDomain", "이메일 도메인을 선택해주세요.");
    }
    if (emailDomain === 'custom' && document.getElementById("customEmailDomain").value.trim() === "") {
        return showError("customEmailDomain", "이메일 도메인을 입력해주세요.", true);
    }
    if (address === "") {
        return showError("address", "주소를 입력해주세요.", true);
    }
    if (detailAddressInput === "") {
        return showError("detailAddressInput", "상세 주소를 입력해주세요.", true);
    }
    if (!maritalStatus) {
        return showError("radioGroup", "결혼 여부를 선택해주세요.");
    }
    if (numDependents === "" || !/^\d+$/.test(numDependents) || parseInt(numDependents) < 0) {
        return showError("numDependents", "유효한 부양 가족 수를 입력해주세요.");
    }
    if (numChildren === "" || !/^\d+$/.test(numChildren) || parseInt(numChildren) < 0) {
        return showError("numChildren", "유효한 자녀 수를 입력해주세요.");
    }
    if (parseInt(numChildren) > parseInt(numDependents)) {
        return showError("numChildren", "자녀 수는 부양 가족 수보다 많을 수 없습니다.");
    }
    if (department === "") {
        return showError("department", "부서를 선택해주세요.");
    }
    if (position === "") {
        return showError("position", "직위를 선택해주세요.");
    }
    if (hireDate === "") {
        return showError("hireDate", "입사일을 선택해주세요.", true);
    }

    // DB 데이터 형식에 맞게 처리
    combineEmail();
    combineDetailAddress();
    formatPhoneNumber();

    return true;
}
// 유효성 검사 - 이름
function validateName(name) {
    // 한글, 영어만 허용 (공백 및 숫자, 특수문자 등 금지)
    const namePattern = /^[가-힣a-zA-Z]+$/;
    return namePattern.test(name);
}

// 유효성 검사 - 생년월일
function validateBirthDate(birth) {
    if (birth === "" || birth.length !== 6 || !/^\d+$/.test(birth)) {
        return false;
    }

    const year = parseInt(birth.substring(0, 2), 10);
    const month = parseInt(birth.substring(2, 4), 10);
    const day = parseInt(birth.substring(4, 6), 10);

    // 00~99의 연도 범위 체크 (1900년대, 2000년대 둘 다 가능)
    const fullYear = year > 50 ? 1900 + year : 2000 + year;

    // 월과 일 범위 체크
    if (month < 1 || month > 12 || day < 1 || day > 31) {
        return false;
    }

    // 실제로 존재하는 날짜인지 체크
    const date = new Date(fullYear, month - 1, day);
    return date.getFullYear() === fullYear && date.getMonth() + 1 === month && date.getDate() === day;
}

// form 제출 처리
function handleFormSubmit(event) {
    event.preventDefault();

    const form = event.target.closest('form');
    const actionUrl = form.action;

    // for (var pair of form.entries()) {
    //     console.log(pair[0] + ': ' + pair[1]);
    // }

    return { form, actionUrl };
}

// AJAX POST 요청 - 사원 등록
function submitInsertForm(event) {
    // 유효성 검사 실행
    if (!validateForm(event)) { return; }

    // form 제출 처리
    const { form, actionUrl } = handleFormSubmit(event);
    const formData = new FormData();

    // FormData 객체에 employee 필드를 추가
    const employee = {
        name: form.name.value,
        birth: form.birth.value,
        residentRegistrationNumber: form.residentRegistrationNumber.value,
        phone: form.phone.value,
        email: form.email.value,
        address: form.address.value,
        detailAddress: form.detailAddress.value,
        department: form.department.value,
        position: form.position.value,
        hireDate: form.hireDate.value,
        maritalStatus: Boolean(parseInt(document.querySelector('input[name="maritalStatus"]:checked').value)),
        numDependents: form.numDependents.value,
        numChildren: form.numChildren.value
    };
    formData.append("employee", new Blob([JSON.stringify(employee)], { type: "application/json" }));

    // FormData 객체에 picture 필드를 추가
    const picture = form.picture.files[0];
    if (picture) {
        formData.append("picture", picture);
    }

    // 데이터를 서버로 전송
    if (confirm("신규 사원 등록하시겠습니까?")) {
        fetch(actionUrl, {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    // 성공 시에는 JSON 데이터를 처리
                    return response.json().then(data => ({
                        status: response.status,
                        data: data
                    }));
                } else {
                    // 오류 시에는 텍스트 메시지 처리
                    return response.text().then(text => ({
                        status: response.status,
                        text: text
                    }));
                }
            })
            .then(response => {
                if (response.status === 200) {
                    alert(response.data.message); // 성공 메시지 알림
                    console.log(response.data.employeeId);
                    if (confirm("급여 정보를 등록하시겠습니까?\n확인을 누르면 등록 페이지로 이동합니다.")) {
                        window.location.href = "/salary/register?employeeId=" + response.data.employeeId;
                    } else {
                        window.location.href = "/employee/" + response.data.employeeId + "/detail";
                    }
                } else {
                    const errorStatuses = [400, 403, 404, 500];
                    if (errorStatuses.includes(response.status)) {
                        alert(response.text); // 오류 메세지 알림
                    } else {
                        alert('사원 등록 중 오류가 발생하였습니다.\n재등록 시도 후 여전히 문제가 발생하면 관리자에게 문의해주세요');
                        window.location.reload();
                    }
                }
            })
            .catch(error => {
                console.error('Error :', error.message);
                alert('오류가 발생하였습니다.\n관리자에게 문의해주세요.');
            });
    }
}

// 유효성 검사 - 사원 정보 수정
function validateEditForm(event) {
    event.preventDefault();

    const name = document.getElementById("name").value.trim();
    const birth = document.getElementById("birth").value.trim();
    const residentRegistrationNumber = document.getElementById("residentRegistrationNumber").value.trim();
    const phoneInput = document.getElementById("phoneInput").value.trim();
    const emailLocal = document.getElementById("emailLocal").value.trim();
    const emailDomain = document.getElementById("emailDomain").value.trim();
    const address = document.getElementById("address").value.trim();
    const detailAddressInput = document.getElementById("detailAddressInput").value.trim();
    const maritalStatus = document.querySelector('input[name="maritalStatus"]:checked');
    const numDependents = document.getElementById("numDependents").value.trim();
    const numChildren = document.getElementById("numChildren").value.trim();
    const remainingLeave = document.getElementById("remainingLeave").value.trim();

    let errorMessage = document.getElementById("error-message");

    if (errorMessage) {
        errorMessage.textContent = "";
    } else {
        console.error("에러 메세지 요소를 찾을 수 없습니다.");
    }

    // 유효성 검사 함수
    function showError(inputId, message, isBottomBorder = false) {
        const inputElement = document.getElementById(inputId);
        errorMessage.textContent = message;

        // 빨간 테두리와 흔들림 효과 추가
        if (isBottomBorder) {
            inputElement.classList.add("input-error-bottom", "shake");
        } else {
            inputElement.classList.add("input-error", "shake");
        }

        // 5초 후 빨간 테두리 제거
        setTimeout(() => {
            inputElement.classList.remove("input-error", "input-error-bottom");
        }, 5000);

        // 애니메이션이 끝난 후 흔들림 제거
        setTimeout(() => {
            inputElement.classList.remove("shake");
        }, 300);

        return false;
    }

    // 각 필드 유효성 검사
    if (name === "" || !validateName(name)) {
        return showError("name", "유효한 이름을 입력해주세요. (한글 또는 영어만 허용)", true);
    }
    if (!validateBirthDate(birth)) {
        return showError("birth", "유효한 생년월일(6자리 숫자)을 입력해주세요.", true);
    }
    if (residentRegistrationNumber === "" || residentRegistrationNumber.length !== 7 || !/^\d+$/.test(residentRegistrationNumber)) {
        return showError("residentRegistrationNumber", "유효한 주민번호 뒷자리(7자리 숫자)를 입력해주세요.", true);
    }
    if (phoneInput === "" || !/^\d{11}$/.test(phoneInput)) {
        return showError("phoneInput", "'-'를 제외한 유효한 전화번호를 입력해주세요.", true);
    }
    if (emailLocal === "") {
        return showError("emailLocal", "이메일을 입력해주세요.", true);
    }
    if (emailDomain === "") {
        return showError("emailDomain", "이메일 도메인을 선택해주세요.");
    }
    if (emailDomain === 'custom' && document.getElementById("customEmailDomain").value.trim() === "") {
        return showError("customEmailDomain", "이메일 도메인을 입력해주세요.", true);
    }
    if (address === "") {
        return showError("address", "주소를 입력해주세요.", true);
    }
    if (detailAddressInput === "") {
        return showError("detailAddressInput", "상세 주소를 입력해주세요.", true);
    }
    if (!maritalStatus) {
        return showError("radioGroup", "결혼 여부를 선택해주세요.");
    }
    if (numDependents === "" || !/^\d+$/.test(numDependents) || parseInt(numDependents) < 0) {
        return showError("numDependents", "유효한 부양 가족 수를 입력해주세요.");
    }
    if (numChildren === "" || !/^\d+$/.test(numChildren) || parseInt(numChildren) < 0) {
        return showError("numChildren", "유효한 자녀 수를 입력해주세요.");
    }
    if (remainingLeave === "" || !/^(\d+(\.[05])?)$/.test(remainingLeave)) {
        console.log(remainingLeave);
        return showError("remainingLeave", "유효한 잔여 연차일을 입력해주세요.");
    }
    if (parseInt(numChildren) > parseInt(numDependents)) {
        return showError("numChildren", "자녀 수는 부양 가족 수보다 많을 수 없습니다.");
    }

    // DB 데이터 형식에 맞게 처리
    combineEmail();
    combineDetailAddress();
    formatPhoneNumber();

    return true;
}

// AJAX PUT 요청 - 사원 정보 수정
function submitUpdateForm(event) {
    // 유효성 검사 실행
    if (!validateEditForm(event)) { return; }

    // form 제출 처리
    const { form, actionUrl } = handleFormSubmit(event);
    const formData = new FormData();

    // FormData 객체에 employee 필드를 추가
    const employee = {
            employeeId: form.employeeId.value,
            name: form.name.value,
            birth: form.birth.value,
            residentRegistrationNumber: form.residentRegistrationNumber.value,
            phone: form.phone.value,
            email: form.email.value,
            address: form.address.value,
            detailAddress: form.detailAddress.value,
            maritalStatus: Boolean(parseInt(document.querySelector('input[name="maritalStatus"]:checked').value)),
            numDependents: form.numDependents.value,
            numChildren: form.numChildren.value,
            remainingLeave: form.remainingLeave.value,
            picture: form['original-picture'].value
    };
    formData.append("employee", new Blob([JSON.stringify(employee)], { type: "application/json" }));

    // FormData 객체에 picture 필드를 추가
    const picture = form.picture.files[0];
    if (picture) {
        formData.append("picture", picture);
    }

    // 수정 성공 후 이동할 URL
    const submitButton = document.querySelector('button[type="submit"]');
    const dataUrl = submitButton.getAttribute('data-url');

    // 데이터를 서버로 전송
    if (confirm("사원 정보를 수정하시겠습니까?")) {
        fetch(actionUrl, {
            method: 'PUT',
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
                    window.location.href = dataUrl;
                } else if (response.status === 404) {
                    alert(response.text); // 404 오류 메세지 알림
                    window.location.reload();
                } else if (response.status === 400) {
                    alert(response.text); // 400 오류 메시지 알림
                } else if (response.status === 500) {
                    alert(response.text); // 500 오류 메시지 알림
                }
            })
            .catch(error => {
                console.error('Error :', error.message);
                alert('오류가 발생하였습니다.\n관리자에게 문의해주세요.');
            });
    }
}

// ==================================================== 각종 함수 ========================================================
// 이메일 조합 함수
function combineEmail() {
    const emailLocal = document.getElementById("emailLocal").value.trim();
    const emailDomain = document.getElementById("emailDomain").value;
    let email = '';
    if (emailDomain === 'custom') {
        const customDomain = document.getElementById("customEmailDomain").value.trim();
        email = emailLocal + "@" + customDomain;
    } else {
        email = emailLocal + "@" + emailDomain;
    }
    document.getElementById("email").value = email;
}

// 이메일 분리 함수
function separateEmail() {
    const email = document.getElementById("email").value;
    if (!email) return;
    const [local, domain] = email.split('@');
    document.getElementById("emailLocal").value = local;
    const emailDomainSelect = document.getElementById("emailDomain");
    if ([...emailDomainSelect.options].some(option => option.value === domain)) {
        emailDomainSelect.value = domain;
    } else {
        emailDomainSelect.value = "custom";
        document.getElementById("customEmailDomain").classList.remove("hidden");
        document.getElementById("customEmailDomain").value = domain;
    }
}

// 이메일 직접 입력 클릭시 입력창 제어 함수
function updateCustomField() {
    const emailDomainSelect = document.getElementById("emailDomain");
    const customDomainInput = document.getElementById("customEmailDomain");
    if (emailDomainSelect.value === "custom") {
        customDomainInput.classList.remove("hidden");
    } else {
        customDomainInput.classList.add("hidden");
        document.getElementById("customEmailDomain").value = '';
    }
}

// 상세주소 조합 함수
function combineDetailAddress() {
    const detailAddressInput = document.getElementById("detailAddressInput").value.trim();
    const extraAddress = document.getElementById("extraAddress").value.trim();
    const detailAddress = detailAddressInput + extraAddress;
    document.getElementById("detailAddress").value = detailAddress;
}

// 전화번호 포맷팅 함수
function formatPhoneNumber() {
    const phoneInput = document.getElementById("phoneInput").value.replace(/\D/g, '');
    const match = phoneInput.match(/^(\d{3})(\d{4})(\d{4})$/); // 3자리-4자리-4자리 매칭
    if (match) {
        document.getElementById("phone").value = match[1] + '-' + match[2] + '-' + match[3];
    } else {
        document.getElementById("phone").value = phoneInput; // 매칭되지 않으면 숫자만 설정
    }
}

// 전화번호 문자열 '-' 제거 함수
function formatInputPhoneNumber(phoneNumber) {
    return phoneNumber.replace(/\D/g, ''); // 숫자 이외의 문자 제거
}

// 페이지에서 전화번호 문자열 '-' 제거 함수
function updatePhoneNumberElement() {
    const phoneInputElement = document.getElementById("phoneInput");
    phoneInputElement.value = formatInputPhoneNumber(phoneInputElement.value);
}

// 기혼 선택 시 부양 가족 수 2로 수정하는 함수
function updateDependents() {
    var isMarried = document.getElementById('marriedYes').checked;
    var numDependentsField = document.getElementById('numDependents');

    if (isMarried) {
        numDependentsField.value = 2;
    } else {
        numDependentsField.value = 1;
    }
}