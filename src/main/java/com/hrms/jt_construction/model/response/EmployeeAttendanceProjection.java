package com.hrms.jt_construction.model.response;

public interface EmployeeAttendanceProjection {
    Long getId();                  // attendance_tbl.id (nullable)
    Long getEmployeeId();          // employee_tbl.id
    String getEmployeeName();      // employee_tbl.name
    String getEmployeeDepartment();// employee_tbl.department
    String getStatus();            // attendance_tbl.status
    String getDate();              // attendance_tbl.date (as String or LocalDate)
    String getRemarks();
}
