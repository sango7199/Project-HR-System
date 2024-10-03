// 버튼들을 마지막 질문과 수평으로 배치
function alignButtonsWithLastQuestion() {
    const lastQuestion = document.querySelector('.survey-container .question-row:last-child');
    const buttonContainer = document.querySelector('.btn-container');

    if (lastQuestion) {
        const questionRect = lastQuestion.getBoundingClientRect();
        const containerRect = document.querySelector('.survey-container').getBoundingClientRect();

        buttonContainer.style.top = (questionRect.top - containerRect.top) + 'px'; // 마지막 질문의 위치에 맞춤
    }
}

// 새로운 질문 추가
function addNewQuestion() {
    const surveyContainer = document.querySelector('.survey-container');
    const questionCount = surveyContainer.querySelectorAll('.question-row').length;

    // 새로운 질문 row 생성
    const newQuestion = document.createElement('div');
    newQuestion.classList.add('question-row');
    newQuestion.innerHTML = `
                <div class="content question">
                    <div class="question-header">
                        <input type="text" id="questionText${questionCount}" name="questionText${questionCount}" placeholder="질문"/>
                        <select id="questionType${questionCount}" name="questionType${questionCount}">
                            <option value="text">단답형</option>
                            <option value="textarea">장문형</option>
                            <option value="radio">단일 선택</option>
                            <option value="checkbox">다중 선택</option>
                            <option value="date">날짜</option>
                            <option value="time">시간</option>
                        </select>
                        <div class="question-remove-btn">
                            <img src="/images/icons/trash.png"/>
                        </div>
                    </div>
                    <div class="options-container" style="display:none;">
                        <div class="option-wrapper">
                            <input type="text" name="option${questionCount}_0" value="옵션 1"/>
                        </div>
                        <div class="option-add-btn">옵션 추가</div>
                    </div>
                </div>
            `;

    // 새로운 질문 추가 후 이벤트 리스너 추가
    surveyContainer.appendChild(newQuestion);

    const newSelect = newQuestion.querySelector(`select[name="questionType${questionCount}"]`);
    newSelect.addEventListener('change', function () {
        toggleOptionsVisibility(this);
    });

    const addOptionBtn = newQuestion.querySelector('.option-add-btn');
    addOptionBtn.addEventListener('click', function () {
        addNewOptionField(this);
    });

    const removeOptionBtn = newQuestion.querySelectorAll('.option-remove-btn');
    removeOptionBtn.forEach(btn => {
        btn.addEventListener('click', function () {
            removeOptionField(this);
        });
    });

    const removeQuestionBtn = newQuestion.querySelector('.question-remove-btn img');
    removeQuestionBtn.addEventListener('click', function () {
        removeQuestion(this);
    });

    alignButtonsWithLastQuestion();
}

// 질문 유형에 따라 옵션 필드를 표시하거나 숨기기
function toggleOptionsVisibility(selectElement) {
    const questionRow = selectElement.closest('.question-row');
    const optionsContainer = questionRow.querySelector('.options-container');

    if (selectElement.value === 'radio' || selectElement.value === 'checkbox') {
        optionsContainer.style.display = 'block';
    } else {
        optionsContainer.style.display = 'none';
    }
    alignButtonsWithLastQuestion();
}

// 새로운 옵션 필드를 추가
function addNewOptionField(button) {
    const optionsContainer = button.previousElementSibling;
    const optionCount = optionsContainer.querySelectorAll('input[type="text"]').length;
    const newOption = document.createElement('div');
    newOption.classList.add('option-row');
    newOption.innerHTML = `
                <input type="text" name="option${optionCount}" value="옵션 ${optionCount + 1}" />
                <span class="option-remove-btn"><img src="/images/icons/minus.png"></span>
            `;

    // 삭제 버튼에 이벤트 리스너 추가
    newOption.querySelector('.option-remove-btn').addEventListener('click', function () {
        removeOptionField(this);
    });

    optionsContainer.appendChild(newOption);
    alignButtonsWithLastQuestion();
}

// 옵션 필드를 삭제
function removeOptionField(button) {
    const optionRow = button.closest('.option-row');
    optionRow.remove();
    alignButtonsWithLastQuestion();
}

// 질문을 삭제
function removeQuestion(button) {
    const questionRow = button.closest('.question-row');
    questionRow.remove();
    alignButtonsWithLastQuestion();
}

// AJAX POST 요청 - 새로운 설문지 등록
function submitSurvey() {
    const surveyData = collectSurveyData();

    // 유효성 검사
    if (!validateSurvey(surveyData)) {
        return;
    }

    fetch('/api/survey', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(surveyData)
    })
        .then(response => response.text().then(data => ({
            status: response.status,
            text: data
        })))
        .then(response => {
            const errorStatuses = [400, 403, 404, 500];
            if (response.status === 200) {
                alert(response.text);
                window.location.href = '/survey/list';
            } else if (errorStatuses.includes(response.status)) {
                alert(response.text);
            } else {
                alert('설문 등록 중 오류가 발생하였습니다.\n재시도 후 여전히 문제가 발생하면 관리자에게 문의해주세요');
                window.location.reload();
            }
        })
        .catch(error => {
            console.error('Error :', error.message);
            alert('오류가 발생하였습니다.\n관리자에게 문의해주세요.');
        });
}

// 설문조사 입력 필드 데이터 수집하는 함수
function collectSurveyData() {
    const surveyTitle = document.querySelector('#title').value;
    const surveyDescription = document.querySelector('#description').value;
    const questions = [];
    const questionRows = document.querySelectorAll('.question-row');
    const expiresAt = document.querySelector('#expiresAt').value;

    questionRows.forEach((questionRow, index) => {
        const questionText = questionRow.querySelector(`input[name="questionText${index}"]`).value;
        const questionType = questionRow.querySelector(`select[name="questionType${index}"]`).value;
        const options = [];

        // 옵션 수집 (radio 또는 checkbox 타입일 경우)
        if (questionType === 'radio' || questionType === 'checkbox') {
            const optionInputs = questionRow.querySelectorAll('.options-container input');
            optionInputs.forEach(optionInput => {
                options.push(optionInput.value);
            });
        }

        questions.push({
            questionText: questionText,
            questionType: questionType,
            options: options
        });
    });

    return {
        title: surveyTitle,
        description: surveyDescription,
        questions: questions,
        expiresAt: expiresAt,
    };
}

// 유효성 검사
function validateSurvey(surveyData) {
    const { title, expiresAt, questions } = surveyData;

    if (!title || title.trim() === "") {
        alert('설문 제목을 입력해주세요.');
        document.querySelector('#title').focus();
        return false;
    }

    if (!expiresAt || expiresAt.trim() === "") {
        alert('설문 종료일을 입력해주세요.');
        document.querySelector('#expiresAt').focus();
        return false;
    }

    if (questions.length === 0) {
        alert('최소한 하나의 질문을 추가해주세요.');
        return false;
    }

    // 각 질문의 내용 유효성 검사
    for (let i = 0; i < questions.length; i++) {
        if (!questions[i].questionText || questions[i].questionText.trim() === "") {
            alert(`질문 ${i + 1}의 내용을 입력해주세요.`);
            document.querySelector(`input[name="questionText${i}"]`).focus();
            return false;
        }

        // 옵션 필드가 있는 경우 (radio, checkbox) 옵션이 누락되지 않았는지 확인
        if ((questions[i].questionType === 'radio' || questions[i].questionType === 'checkbox') && questions[i].options.length === 0) {
            alert(`질문 ${i + 1}에 최소한 하나의 옵션을 추가해주세요.`);
            document.querySelector(`input[name="option${i}_0"]`).focus();
            return false;
        }
    }

    return true;
}