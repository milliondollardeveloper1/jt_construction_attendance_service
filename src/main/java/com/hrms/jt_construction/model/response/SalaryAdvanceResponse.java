package com.hrms.jt_construction.model.response;

import lombok.Data;

@Data
public class SalaryAdvanceResponse {
    private float amount;
    private String issuedOn;
    private String remarks;
    private String employeeName;
    private long employeeId;
    private String employeeDepartment;
    private long id;
}
