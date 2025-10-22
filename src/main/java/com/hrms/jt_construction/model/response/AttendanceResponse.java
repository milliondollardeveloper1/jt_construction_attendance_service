package com.hrms.jt_construction.model.response;

import lombok.Data;

@Data
public class AttendanceResponse {
    private String status;
    private String date;
    private String remarks;
    private String employeeName;
    private long employeeId;
    private String employeeDepartment;
    private long id;
}
