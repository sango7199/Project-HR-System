package com.woosan.hr_system.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessTrip {
    private int mapId;
    private String employeeId;
    private int contactId;
    private LocalDate visitDate;
    private String address;
    private String detailedAddress;
    private LocalDateTime createdDate;
    public enum Status {
        미방문,
        방문_완료
    }
}
