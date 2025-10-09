package com.hrms.jt_construction.model.request;

import com.hrms.jt_construction.jpa.Employee;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SalaryAdvanceRequest {
    private float amount;
    private String issuedOn;
    private String remarks;
    private long uniqueID;
    private long employeeId;
}
