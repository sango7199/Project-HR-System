// 연봉 포맷팅 함수
function formatAnnualSalary(value) {
    return value.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

// 테이블의 모든 연봉 값을 포맷팅
function formatSalaries() {
    const salaryCells = document.querySelectorAll('.annualSalary');
    salaryCells.forEach(cell => {
        const rawValue = cell.textContent.trim();
        cell.textContent = formatAnnualSalary(rawValue);
    });
}