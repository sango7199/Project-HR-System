package com.woosan.hr_system.attendance.model;

import com.woosan.hr_system.employee.model.Employee;
import com.woosan.hr_system.vacation.model.Vacation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Attendance {
    private int attendanceId;           // 근태 ID
    private String employeeId;          // 사원 ID
    private LocalDate date;             // 날짜
    private LocalTime checkIn;          // 출근 시간
    private LocalTime checkOut;         // 퇴근 시간
    private String status;              // 근태 상태
    private float workingHours;         // 근무한 정규 근무시간
    private Integer overtimeId;         // 초과근무 ID
    private Integer vacationId;         // 휴가 ID
    private Integer tripId;             // 출장 ID
    private String notes;               // 메모
    private LocalDateTime lastModified; // 마지막 수정 일시
    private String modifiedBy;          // 마지막 수정 사원 ID

    private Employee employee;          // 사원 정보
    private Vacation vacation;          // 휴가 정보
    private Overtime overtime;          // 초과근무 정보
}
