package com.hrms.jt_construction.controller;

import com.hrms.jt_construction.jpa.Department;
import com.hrms.jt_construction.model.request.AttendanceRequest;
import com.hrms.jt_construction.model.request.DepartmentRequet;
import com.hrms.jt_construction.model.request.EmployeeRequest;
import com.hrms.jt_construction.model.request.SalaryAdvanceRequest;
import com.hrms.jt_construction.model.response.*;
import com.hrms.jt_construction.service.HRMSService;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/common")
public class HRMSController {

    @Autowired
    HRMSService hrmsService;

    @GetMapping("/validate")
    public ResponseEntity<ResponseMessage> validateAuthStatus() {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Authentication Status is successful");
        return  new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/create-department")
    public ResponseEntity<?> addDepartment(@RequestBody DepartmentRequet requet) {
        try {
            String message = this.hrmsService.createNewDepartment(requet);
            if (message.contains("success"))
                return ResponseEntity.ok(new ResponseMessage(message));
            return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to create departments."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetch-departments")
    public ResponseEntity<?> getDepartments() {
        List<DepartmentsResponse> departmentsResponse = null;
        try {
            departmentsResponse = this.hrmsService.getAllDepartmetns();
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to fetch departments."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(departmentsResponse);
    }

    @PutMapping("/update-department")
    public ResponseEntity<?> updateDepartment(@RequestBody DepartmentRequet request) {
        try {
            String message = this.hrmsService.editDepartment(request);
            if (message.contains("success"))
                return ResponseEntity.ok(new ResponseMessage(message));
            return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to update departments."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-department")
    public ResponseEntity<?> deleteDepartment(@RequestBody DepartmentRequet request) {
        String message = null;
        try {
            message = this.hrmsService.deleteDepartment(request);
            if (message.contains("success"))
                return ResponseEntity.ok(new ResponseMessage(message));
            return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to delete departments."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetch-employees")
    public ResponseEntity<?> getEmployees() {
        List<EmployeeResponse> employeeResponse = null;
        try {
            employeeResponse = this.hrmsService.getAllEmployeess();
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to fetch employees."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(employeeResponse);
    }

    @GetMapping("/get-employees-by-department")
    public ResponseEntity<?> getEmployees(@RequestParam String departmentName) {
        List<EmployeeResponse> employeeResponse = null;
        try {
            employeeResponse = this.hrmsService.getAllEmployeessByDepartment(departmentName);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to fetch employees."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(employeeResponse);
    }

    @PostMapping("/create-employee")
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeRequest requet) {
        try {
            ResponseMessage message = this.hrmsService.createNewDEmployee(requet);
            if (message.getCreatedID() > -1)
                return ResponseEntity.ok(message);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to create employee."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-employee")
    public ResponseEntity<?> updateEmployee(@RequestBody EmployeeRequest request) {
        try {
            String message = this.hrmsService.editDEmployee(request);
            if (message.contains("success"))
                return ResponseEntity.ok(new ResponseMessage(message));
            return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to update employee."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-employee")
    public ResponseEntity<?> deleteEmployee(@RequestBody EmployeeRequest request) {
        String message = null;
        try {
            message = this.hrmsService.deleteEmployee(request);
            if (message.contains("success"))
                return ResponseEntity.ok(new ResponseMessage(message));
            return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("This employee record cannot be deleted because it is linked to other data"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/fetch-salary-advance")
    public ResponseEntity<?> getSalaryAdvance() {
        List<SalaryAdvanceResponse> salaryAdvanceResponses = null;
        try {
            salaryAdvanceResponses = this.hrmsService.getAllSalaryAdvances();
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to fetch salary advance."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(salaryAdvanceResponses);
    }

    @PostMapping("/create-salary-advance")
    public ResponseEntity<?> addDalaryAdvance(@RequestBody SalaryAdvanceRequest requet) {
        try {
            ResponseMessage message = this.hrmsService.createNewDSalaryAdvance(requet);
            if (message.getCreatedID() > -1)
                return ResponseEntity.ok(new ResponseMessage(message.getMessage()));
            return new ResponseEntity<>(new ResponseMessage(message.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to create salary advance."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-salary-advance")
    public ResponseEntity<?> updateSalaryAdvance(@RequestBody SalaryAdvanceRequest request) {
        try {
            String message = this.hrmsService.editSalaryAdvance(request);
            if (message.contains("success"))
                return ResponseEntity.ok(new ResponseMessage(message));
            return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to update salary advance."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-salary-advance")
    public ResponseEntity<?> deleteSalaryAdvance(@RequestBody SalaryAdvanceRequest request) {
        try {
            String message = this.hrmsService.deleteSalaryAdvance(request);
            if (message.contains("success"))
                return ResponseEntity.ok(new ResponseMessage(message));
            return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to delete salary advance."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-attendance")
    public ResponseEntity<?> getAttendance(@RequestParam String date, @RequestParam(defaultValue = "ALL") String department) {
        List<EmployeeAttendanceProjection> attendanceResponses = null;
        try {
            attendanceResponses = this.hrmsService.getAllAttendance(date, department);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to fetch attendance records."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(attendanceResponses);
    }

    @PutMapping("/update-attendance")
    public ResponseEntity<?> updateAttendance(@RequestBody List<AttendanceRequest> request) {
        try {
            String message = this.hrmsService.updateAttendance(request);
            if (message.contains("success"))
                return ResponseEntity.ok(new ResponseMessage(message));
            return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to update attendace records."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-report")
    public ResponseEntity<?> getReport(@RequestParam int month, @RequestParam int year, @RequestParam(defaultValue = "ALL") String department) {
        List<MonthlySalaryCalculationProjection> responses = null;
        try {
            responses = this.hrmsService.generateEmployeesReport(month, year, department);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to fetch salary advance."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(responses);
    }

}
