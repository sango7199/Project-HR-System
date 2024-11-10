# HR 솔루션 웹 애플리케이션 - HaruHaru
## 프로젝트 설명
정회산은 취업 준비를 위한 포트폴리오 제작을 목표로 하였고, 진승우와 협업하여 HR 시스템과 업무 관리 기능을 결합한 프로젝트를 개발하게 되었습니다. 
이 프로젝트는 회산의 기획과 설계를 중심으로, 승우가 다양한 기술을 실습하며 실무 역량을 쌓아가는 경험을 통해 두 사람 모두 실질적인 성장을 이루는 것을 목표로 하고 있습니다.

**기능 소개를 제외한 프로젝트 개요와 상세 정보는 [노션 문서](https://awake-reaction-86b.notion.site/HaruHaru-Project-with-160975df7b7b4aa483d2d3598cbd7ee2?pvs=4)를 참고해 주세요. 자세한 내용은 링크에서 확인하실 수 있습니다.**

## 프로젝트 기능 소개 
## 정회산 담당 기능
### 1. 방문자 회원가입 및 로그인
#### 방문자 회원가입 : 방문자 회원가입을 통해 손쉽게 사원 계정을 생성할 수 있습니다.
#### 로그인 : 사원 아이디와 비밀번호을 통해 로그인할 수 있습니다.
- 아이디를 기억하는 옵션을 통해 더 편리하게 로그인할 수 있습니다.

![join-and-login](https://github.com/user-attachments/assets/5f8d7dfb-acaa-422c-aa30-aa883ace94e6)
#### 보안을 위해 처음 로그인하시는 경우 비밀번호 변경 창이 자동으로 표시됩니다.
![first-password-change](https://github.com/user-attachments/assets/e3e1b685-0253-47bd-9764-46b881cf683e)
### 2. 근태 관리
#### 출근 퇴근 기록
- 출퇴근 창을 통해 출퇴근 시간과 이번 주 근무 시간을 간편하게 확인하고, 출퇴근 기록을 손쉽게 등록할 수 있습니다. 
- 정규 출근 시간 이후에 출근할 경우 자동으로 지각 처리됩니다. 
- 정규 퇴근 시간 이전에 퇴근할 경우 자동으로 조퇴 처리됩니다. 
- 정규 퇴근 시간 이후에 퇴근할 경우 자동으로 초과근무가 적용되어 처리됩니다.
- 영업일 오전 9시에 사원이 휴가 상태일 경우 자동으로 휴가 처리됩니다.
- 반차일에는 정규 출근 시간이 조정되어 지각 및 조퇴 처리에서 제외됩니다.

![commute](https://github.com/user-attachments/assets/a80a0df7-0a22-442f-85ee-3682b578a960)

- 영업일 오후 6시에 미출근 상태인 경우 자동으로 결근 처리됩니다.

<img width="45%" alt="스크린샷 2024-11-06 오후 6 45 33" src="https://github.com/user-attachments/assets/219a435f-240e-4e82-a3a2-cfe40d335b3e">
<img width="45%" alt="스크린샷 2024-11-06 오후 6 45 56" src="https://github.com/user-attachments/assets/fe135cea-993c-4c7a-a8dc-b05912203119">
<img width="1166" alt="스크린샷 2024-11-06 오후 6 29 58" src="https://github.com/user-attachments/assets/260caeef-e1f9-41d9-aa05-547c1d3529c5">

#### 조퇴 기록 : 조퇴창을 통해 조퇴 사유를 등록 후 조퇴할 수 있습니다. 
![early-leave](https://github.com/user-attachments/assets/87b8d230-a828-47dd-9ca5-0776ca6534cf)
#### 나의 근태 현황을 월별로 조회할 수 있으며, 일별 근태 기록을 클릭하여 해당 일의 상세 정보를 확인할 수 있습니다.
<img width="1278" alt="스크린샷 2024-11-06 오후 2 26 41" src="https://github.com/user-attachments/assets/bd2ce81f-b35f-4e66-a6a8-63a4612f47da"/>

#### 오늘의 근태 현황(인사부서 권한 필요), 부서 관리 - 오늘의 근태 현황(관리자 권한 필요)
- 오늘의 근태 현황을 통계표와 도넛 차트를 통해 조회할 수 있습니다.
- 정상 출근, 근태 불량(지각, 조퇴, 결근), 출장 및 휴가 사원을 자세하게 조회할 수 있습니다.

#### 근태 목록(인사부서 권한 필요), 부서 관리 - 근태 관리(관리자 권한 필요)
- 모든 근태 기록을 조회할 수 있습니다.
- 부서별, 근태 상태별, 월별, 검색어를 통한 필터링 기능을 제공합니다.
- 부서 관리 - 근태 관리에서 부서원들의 근태를 조회할 수 있습니다.

![attendance-list](https://github.com/user-attachments/assets/21c6b694-f394-41f2-b70e-1221641c4b72)

- 행을 클릭하면 근태 기록의 상세 조회 및 수정이 가능합니다.
- 정규 퇴근 시간 이후로 퇴근 시간을 수정 시 자동으로 초과근무가 적용되어 처리됩니다.

![attendance-detail-update](https://github.com/user-attachments/assets/950d0243-3c20-4614-950e-95d49689fbc0)

#### 초과근무 목록(인사부서 권한 필요)
- 모든 초과근무 기록을 조회할 수 있으며, 기록을 수정 및 삭제할 수 있습니다.
- 부서별, 월별, 검색어를 통한 필터링 기능을 제공합니다.

![overtime-update](https://github.com/user-attachments/assets/6266cabd-093a-4d7a-91b3-d7999ce5d407)

#### 공휴일 관리(인사부서 권한 필요)
- 모든 공휴일을 조회할 수 있으며, 인사부서 관리자 권한이 있다면 등록, 수정, 삭제할 수 있습니다.
- 공휴일이 등록되면 모든 사원의 캘린더에 자동으로 표시됩니다.

![holiday](https://github.com/user-attachments/assets/27540968-d953-43cf-9790-81a637ae6c59)

### 3. 사원 관리(인사부서 권한 필요)
#### 사원 목록 조회
- 모든 사원 정보를 조회할 수 있습니다.
- 사원 행을 선택하면 사원의 상세 조회 및 추가 작업을 진행할 수 있습니다.
- 부서별, 월별, 재직 상태, 검색어를 통한 필터링 기능을 제공합니다.
- 사원 정보를 상세조회하면 사원 정보 수정, 퇴사 정보 수정(재직상태가 퇴사일 경우), 재직 상태 변경, 승진 처리, 계정 잠금/해제 등 작업을 할 수 있습니다.

![employee-list](https://github.com/user-attachments/assets/b71c49ba-6cfa-4b07-8b5c-94ade3780fd8)

#### 사원 정보 추가 작업
- 사원의 재직 상태를 변경할 수 있습니다.

![status-update](https://github.com/user-attachments/assets/f314fd57-e5e7-4cc8-83f7-d1984b8e9e43)

- 사원의 직급을 한 단계 승진 처리할 수 있습니다.

![promotion](https://github.com/user-attachments/assets/129a4eee-c869-4de1-b27b-54198a43ebb8)

- 사원의 계정을 잠금 또는 잠금해제 할 수 있습니다.

![account-lock](https://github.com/user-attachments/assets/0a1a3cf6-2e59-41d3-a026-75bab8f62412)
![account-unlock](https://github.com/user-attachments/assets/211245af-052d-44d9-b5c7-a75b7d80fd16)

#### 사원 정보 수정 : 사원 정보를 수정할 수 있습니다.
<img width="1264" alt="스크린샷 2024-11-06 오후 7 17 10" src="https://github.com/user-attachments/assets/9ca4b6e9-980d-4ac7-84af-2e87311a193b">

#### 사원 등록 : 신규 사원의 정보를 등록할 수 있습니다.
![employee-register](https://github.com/user-attachments/assets/febc71bc-4fc0-498a-8fe3-87f97efef2c5)

#### 퇴사 사원 관리
- 퇴사 예정인 사원, 퇴사 처리된 사원, 퇴사 후 1년이 경과된 사원을 조회할 수 있습니다. 
- 퇴사 예정 사원을 퇴사처리 할 수 있습니다.
- 퇴사 후 1년이 경과된 사원의 정보를 영구 삭제할 수 있습니다.

![resignation](https://github.com/user-attachments/assets/adb626cb-fdf5-451d-814d-a260d2308752)

#### 퇴사 정보 수정 : 사원의 퇴사 정보와 첨부된 문서를 수정할 수 있습니다.
<img width="1285" alt="스크린샷 2024-11-06 오후 11 11 23" src="https://github.com/user-attachments/assets/66973fee-30ca-43a8-9a2e-22fe0f94183c">

#### 내 정보 - 내 정보 관리
- 내 정보를 조회할 수 있으며 내 정보를 수정할 수 있습니다.

![myinfo](https://github.com/user-attachments/assets/703ee518-d3f2-4888-8c1e-54da9e44c6d6)

- 내 비밀번호의 강도와 마지막 비밀번호 변경 날짜를 조회할 수 있으며 비밀번호를 변경할 수 있습니다.
- 스프링 시큐리티의 암호화 기능을 활용하여 비밀번호 변경 시 보안을 강화하고, 사용자 계정을 안전하게 보호합니다.
- 비밀번호를 변경한 지 3개월이 경과 후 로그인 시 비밀번호 변경창을 표시합니다.

![password](https://github.com/user-attachments/assets/34740f00-a970-4f37-b21a-61fdf90acb56)

### 4. 휴가 관리
#### 휴가 신청
- 본인 휴가를 신청할 수 있습니다.
- 휴가를 연차로 신청하면 주말과 공휴일은 자동으로 계산하여 연차에서 제외합니다.
- 반차 신청 시 0.5 연차로 자동 계산됩니다.
- 연차와 반차를 제외하곤 연차를 사용하지 않습니다.
- 연차가 없다면 연차 휴가를 사용할 수 없습니다.

![vacation-request](https://github.com/user-attachments/assets/c296dc9c-f2b6-402c-b18c-38775ed6283e)

#### 휴가 내역
- 나의 휴가 내역과 잔여 연차를 조회할 수 있습니다.
- 날짜 필터링 기능을 제공합니다.

<img width="1082" alt="스크린샷 2024-11-07 오전 12 04 22" src="https://github.com/user-attachments/assets/3f376f9e-ea0a-44bc-bae9-f4ddfb8528d4">

#### 휴가 목록(인사부서 권한 필요)
- 모든 사원의 휴가 내역을 조회할 수 있습니다.
- 각 행을 선택하면 사원의 자세한 휴가 신청을 조회할 수 있으며 수정 및 삭제할 수 있습니다.
- 부서별, 승인상태, 날짜, 검색어 필터링을 제공합니다.

<img width="1273" alt="스크린샷 2024-11-07 오전 12 14 20" src="https://github.com/user-attachments/assets/fed70dc3-2dda-4e65-9ecb-a558dd48d86b">

#### 휴가 등록(인사부서 권한 필요)
- 휴가 신청 기능을 포함합니다.
- 타인 휴가를 등록할 수 있습니다.

![vacation-request](https://github.com/user-attachments/assets/6a36eef9-cf33-46f7-8e68-112b42ec9307)

#### 부서 관리 - 휴가 관리(관리자 권한 필요)
- 부서원의 휴가 내역을 조회할 수 있습니다.
- 각 행을 선택하면 사원의 자세한 휴가 신청을 조회할 수 있으며 수정, 삭제, 승인, 거절 작업을 진행할 수 있습니다.
- 연차 휴가라면 승인처리 시 해당 사원의 연차가 차감됩니다.
- 승인상태, 날짜 필터링을 제공합니다.

![vacation-department](https://github.com/user-attachments/assets/b4794c9c-4a6a-4510-90c8-f840d8e2f247)

### 5. 급여 관리
#### 급여정보 목록
- 모든 사원의 급여정보들과 급여 정보 미등록 사원들을 조회할 수 있습니다.
- 사용 여부, 검색어 필터링을 제공합니다.

![salary-list](https://github.com/user-attachments/assets/2228af66-4804-4bb8-aaf6-076341dab124)

#### 급여정보 등록
- 사원의 급여 정보를 등록할 수 있습니다.
- 급여정보 목록에서 급여정보가 미등록된 특정 사원의 급여 정보를 등록할 수 있습니다.
- 급여정보 등록 시 사원의 급여가 존재한다면 현재 급여정보를 정지 후 새로 등록할 수 있습니다.

![salary-add](https://github.com/user-attachments/assets/658c47c7-36a1-40e0-96db-3d95d1983af0)

#### 급여 지급 내역
- 모든 사원의 급여 지급 내역을 조회할 수 있습니다.
- 부서별, 월별 검색어 필터링을 제공합니다.
- 급여가 미지급된 사원들의 선택 버튼이 활성화 되고 지급된 사원들은 비활성화됩니다.
- 선택 버튼이 활성화된 사원들에겐 급여와 공제 비율에 맞춰 급여를 지급할 수 있습니다.
- 급여 지급을 성공하면 자동으로 급여명세서가 생성됩니다.

![pay](https://github.com/user-attachments/assets/a4e6d7c3-1f83-485f-be5f-4f0a4f129dca)

#### 급여명세서 목록
- 모든 사원의 급여명세서를 조회할 수 있습니다.
- 월별, 부서별, 검색어 필터링을 제공합니다.
- 급여명세서를 상세 조회할 수 있으며, 수정과 삭제할 수 있습니다.
- 급여명세서 PDF 저장과 인쇄하기 기능을 제공합니다.

![payslip](https://github.com/user-attachments/assets/0517d3ea-4b20-4761-bc90-2b6109c5b532)

#### 급여 및 공제 비율(수정 시 인사부서 관리자 권한 필요)
- 급여와 공제의 비율을 조회하고 수정할 수 있습니다.

![ratio](https://github.com/user-attachments/assets/d9104352-1c53-4e18-b20c-616938077e35)

#### 내 급여 관리
- 내 급여 정보 내역을 확일할 수 있으며 현재 급여 정보를 수정할 수 있습니다.
- 나의 급여명세서 기록을 확인할 수 있습니다.

<img width="1083" alt="image" src="https://github.com/user-attachments/assets/f5f8ea1b-149f-4cb9-8e8a-ad354a173e13">

### 6. 설문 조사
#### 설문 등록(관리자 권한 필요)
- 새로운 설문을 등록할 수 있습니다.
- 질문 종류는 단답형, 장문형, 단일선택형, 다중선택형, 날짜형, 시간형이 있습니다.

![survey-create](https://github.com/user-attachments/assets/61d22e98-a36f-4c17-afad-9262f41761cb)

#### 설문 참여 : 설문조사에 참여할 수 있습니다.
![survey](https://github.com/user-attachments/assets/89a2c559-a0b9-474e-b4f5-747391892f79)

#### 설문 응답 및 통계 조회(설문 등록자 권한 필요)
- 응답 : 설문에 참여한 사원과 총 응답 수, 응답을 개별 조회할 수 있습니다.
- 통계 : 설문 응답에 대한 통계를 조회할 수 있습니다.

![stats](https://github.com/user-attachments/assets/99d02b69-967d-47fb-8610-a77bb67455d1)

### 7. 알림 수신함
- 자신에게 온 알림을 확인할 수 있습니다.
- 읽지 않은 알림은 파란 점이 표시되며 알림을 읽으면 파란 점이 사라집니다.
- 알림에 마우스를 올리면 알림의 전체 메세지와 삭제 버튼이 표시됩니다.<br>개별 알림을 클릭하면 알림이 읽음 처리되고 URL이 연결되어 있으면 해당 URL로 이동합니다.
- 모두 읽기, 모두 삭제 기능을 제공합니다.

![notification](https://github.com/user-attachments/assets/e97fd1a3-c528-40ca-b8eb-306e561cd744)

## 진승우 담당 기능
### 1. 보고서
#### 내 보고서 : 전반적으로 내 보고서에 대해 조회할 수 있습니다.
- 최근 작성한 보고서 목록 : 내가 가장 최근에 작성한 보고서 5개를 보여줍니다.
    - '더보기'를 클릭할 경우, 보고서 목록 화면으로 이동하여 전체적인 보고서 목록들을 보여줍니다.
    - 보고서를 클릭할 경우, 보고서에 대한 세부적인 정보 조회가 가능합니다.
- 최근 수신된 요청 목록 : 내가 가장 최근에 받은 보고서 작성 요청 5개를 보여줍니다.
    - 더보기 버큰을 클릭할 경우, 수신된 요청 목록 화면으로 이동하여 전체적인 요청 목록들을 보여줍니다.
    - 요청을 클릭할 경우, 요청에 대한 세부적인 정보 조회가 가능합니다.
- 작성한 보고서 통계 : 내가 작성한 보고서를 월 별로 나타내 줍니다.
    - 작성일 검색 기능을 추가하여 보고서 통계를 세부적으로 조회할 수 있습니다.

<img width="1274" alt="image" src="https://github.com/user-attachments/assets/a3be1e50-7ddc-4579-9c0e-a2a59b3b64ce">

#### 보고서 작성
- 결재자는 부서를 선택한 후, 선택하게 하여 내가 원하는 결재자를 쉽게 찾을 수 있도록 하였습니다.
- 필요한 정보는 다 입력하였는지, 업무 완료 날짜는 오늘 날짜로부터 이전의 날짜인지 확인하여 보고서를 작성할 수 있도록 하였습니다.
- 첨부파일은 최대 3개, 각 10MB 이하의 파일만 업로드할 수 있도록 하였습니다. 중복된 파일은 업로드하지 못하도록 하였습니다.
- 보고서 작성 시 결재자에게 '보고서 결재 요청이 도착했습니다. 작성자 : OOO(이름)'라는 알림을 생성합니다.

![report-write](https://github.com/user-attachments/assets/6d2efe57-58ad-4a8e-b267-f4c84892602a)

#### 보고서 목록
- 목록 갯수, 결재상태, 작성일, 검색어 등을 설정하여 내가 원하는 목록을 구성할 수 있습니다.
- 목록 내의 보고서를 클릭 할 경우 보고서에 대한 세부조회가 가능하며, 보고서 수정 및 삭제가 가능합니다.
    - 보고서 수정 : 보고서 정보에 대한 수정이 가능합니다. 유효성검사 및 파일 업로드 규칙은 보고서 작성 시와 같은 사항을 갖고 있습니다.

![report-list](https://github.com/user-attachments/assets/297a3d5b-570c-4dbe-bb32-4cf6d62a3677)

#### 수신된 요청 목록 조회
- 목록 갯수, 요청일, 검색어 등을 설정하여 내가 원하는 목록을 구성할 수 있습니다.
- 보고서 처리 탭의 '새 보고서 작성' 버튼을 통해 요청과 연계된 보고서를 작성할 수 있습니다.
    - 요청과 연계된 보고서 작성 시, 요청 내용을 확인할 수 있으며 결재자는 자동으로 요청자로 설정됩니다.

![report-request](https://github.com/user-attachments/assets/7b09d35e-1e83-4997-a02e-dd619e9f589b)

### 2. 보고서 요청(관리자 권한 필요)
#### 결재 및 요청 사항
- 결재 대기 중인 보고서 목록 : 나에게 온 결재 요청 대기 중인 보고서를 보여줍니다.
    - 더보기 버튼 클릭 시, 보고서 결재 목록 화면으로 이동합니다.
    - 보고서 클릭 시, 결재가 가능한 화면이 띄워집니다.
- 요청에 따른 보고서 작성 목록 : 내가 보낸 요청에 따라 쓰여진 보고서 목록을 보여줍니다.
    - 더보기 버튼 클릭 시, 발신된 요청 목록 조회 화면으로 이동합니다.
    - 작성한 보고서 조회 버튼 클릭 시, 그 보고서 조회가 가능합니다.
- 부서 보고서 통계 : 내가 속한 부서의 보고서 통계 조회가 가능합니다.
    - 작성일, 사원 등을 선택하여 내가 원하는 직원의 보고서 통계를 조회할 수 있습니다.

<img width="1274" alt="image" src="https://github.com/user-attachments/assets/7b758892-e8a2-41ff-95c5-b1f056242523">

#### 보고서 작성 요청
- 요청 대상자(보고서 작성자)는 부서 선택 후, 선택하게 하여 내가 원하는 대상자를 쉽게 찾을 수 있도록 하였습니다.
- 필요한 정보는 다 입력하였는지, 마감일은 오늘 날짜로부터 이후의 날짜를 입력하였는지 확인하여 요청을 생성할 수 있도록 하였습니다.
- 보고서 작성 요청 완료 시, 대상자(보고서 작성자)에게 '보고서 작성 요청이 있습니다. 요청자 : OOO(이름)'라는 알림을 생성합니다.

![request](https://github.com/user-attachments/assets/77c0e76d-aa51-4382-aaaf-3701069064da)

#### 보고서 결재 목록 조회
- 목록 갯수, 결재상태, 작성일, 검색어 등을 입력하여 내가 원하는 결재 목록을 검색할 수 있도록 하였습니다.
- 보고서 클릭 시, 보고서 결재처리를 할 수 있도록 하였습니다.

![approval](https://github.com/user-attachments/assets/9268d22c-b1af-49a3-940a-efaa77c2461f)

#### 발신된 요청 목록
- 목록 갯수, 요청일, 검색어 등을 입력하여 내가 원하는 요청 목록을 구성할 수 있습니다.
- 요청을 클릭하여 수정, 삭제가 가능합니다.
- 만약 내 요청에 따라 작성된 보고서가 있다면 '작성한 보고서 조회' 버튼을 통해 조회가 가능합니다.

![ㅁㄹ](https://github.com/user-attachments/assets/60046817-3f02-4b90-8365-c1fc1285698f)

### 3. 일정
#### 일정 목록
- 오늘의 일정 : 생성일이 오늘인 일정들을 나타내줍니다.
- '일정 등록' 버튼 클릭 시, 일정을 생성하는 화면을 띄워줍니다.
- 월, 목록 버튼을 통해 일정을 달력 형태 또는 목록 형태로 표시해줍니다.
- 일정 클릭 시, 일정에 대한 세부 조회가 가능하며 수정, 삭제, 일정 상태변경이 가능합니다.
    - 출장에 대한 세부 조회 및 수정, 삭제, 출장 상태변경이 가능합니다.

<img width="1274" alt="image" src="https://github.com/user-attachments/assets/005b8348-5c54-49ba-9685-cc537ce9b198">
<img width="1274" alt="image" src="https://github.com/user-attachments/assets/20014c91-d20d-4197-ad93-7d1d3315e944">

#### 일정 등록
- 일정 색상을 통해 중요한 일정 및 비슷한 일정을 구분해 시각적으로 강조할 수 있습니다.
- 필요한 정보를 다 입력하였는지, 시작일이 종료일 보다 빠르거나 같은지 확인 후 일정을 등록할 수 있도록 하였습니다.
- '추가' 버튼을 통해 프로젝트 및 출장정보를 입력할 수 있도록 하였습니다.
    - 필요한 정보가 다 입력되었는지, 전화번호 형태가 올바른지, 이메일 형태가 올바른지 등을 확인하여 출장 정보를 일정에 등록할 수 있도록 하였습니다.
  
<img width="1274" alt="image" src="https://github.com/user-attachments/assets/dc02b5b3-8952-4277-a261-c62dc2606ede">
