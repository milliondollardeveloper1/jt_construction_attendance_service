package com.hrms.jt_construction.model.response;

import lombok.Data;

import java.math.BigInteger;

@Data
public class DepartmentsResponse {
    private int departmentId;
    private String departmentName;
    private String salaryType;
}
