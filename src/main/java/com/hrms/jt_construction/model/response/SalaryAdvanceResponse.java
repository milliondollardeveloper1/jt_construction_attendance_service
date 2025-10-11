package com.hrms.jt_construction.model.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SalaryAdvanceResponse {
    private float amount;
    private LocalDate issuedOn;
    private String remarks;
    private String employeeName;
    private long employeeId;
    private String employeeDepartment;
    private long id;
}
