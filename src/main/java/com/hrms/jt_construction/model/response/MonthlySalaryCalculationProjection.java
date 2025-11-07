package com.hrms.jt_construction.model.response;

import java.math.BigDecimal;

public interface MonthlySalaryCalculationProjection {
    Long getEmployeeId();
    String getEmployeeName();
    String getDepartment();
    BigDecimal getMonthlySalary();
    Integer getAttendanceMonth();
    Integer getAttendanceYear();
    Long getTotalPresentDays();
    Integer getTotalDaysInMonth();
    BigDecimal getTotalAdvanceDeduction();
    BigDecimal getActualSalary();
    String getSalaryType();
}
