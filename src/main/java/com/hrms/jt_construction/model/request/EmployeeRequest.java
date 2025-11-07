package com.hrms.jt_construction.model.request;

import lombok.Data;

@Data
public class EmployeeRequest {
    long uniqueID;
    String name;
    String mobile;
    String department;
    int hoursOfWork;
    int salary;
    String salaryType;
}
