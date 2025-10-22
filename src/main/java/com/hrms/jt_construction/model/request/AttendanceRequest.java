package com.hrms.jt_construction.model.request;

import lombok.Data;

@Data
public class AttendanceRequest {
    private String status;
    private String date;
    private String remarks;
    private long uniqueID;
    private long employeeId;
}
