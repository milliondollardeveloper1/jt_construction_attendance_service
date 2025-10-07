package com.hrms.jt_construction.model.response;

import lombok.Data;

@Data
public class EmployeeResponse {
    long id;
    String name;
    String mobile;
    String department;
    int hoursOfWork;
    int salary;
}
