package com.hrms.jt_construction.model.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SalaryAdvanceRequest {
    private float amount;
    private LocalDate issuedOn;
    private String remarks;
    private long uniqueID;
    private long employeeId;
}
