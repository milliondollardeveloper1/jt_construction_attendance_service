package com.hrms.jt_construction.controller;

import com.hrms.jt_construction.jpa.Department;
import com.hrms.jt_construction.model.request.DepartmentRequet;
import com.hrms.jt_construction.model.request.EmployeeRequest;
import com.hrms.jt_construction.model.response.DepartmentsResponse;
import com.hrms.jt_construction.model.response.EmployeeResponse;
import com.hrms.jt_construction.model.response.ResponseMessage;
import com.hrms.jt_construction.service.HRMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to fetch departments."), HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to fetch departments."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-department")
    public ResponseEntity<?> deleteDepartment(@RequestBody DepartmentRequet request) {
        String message = null;
        try {
            message = this.hrmsService.deleteDepartment(request);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(new ResponseMessage(message));
    }

    @GetMapping("/fetch-employees")
    public ResponseEntity<?> getDEmployees() {
        List<EmployeeResponse> employeeResponse = null;
        try {
            employeeResponse = this.hrmsService.getAllEmployeess();
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to fetch departments."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(employeeResponse);
    }

    @PostMapping("/create-department")
    public ResponseEntity<?> addDepartment(@RequestBody EmployeeRequest requet) {
        try {
            String message = this.hrmsService.createNewDEmployee(requet);
            if (message.contains("success"))
                return ResponseEntity.ok(new ResponseMessage(message));
            return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to fetch departments."), HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to fetch departments."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-department")
    public ResponseEntity<?> deleteEmployee(@RequestBody EmployeeRequest request) {
        String message = null;
        try {
            message = this.hrmsService.deleteEmployee(request);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(new ResponseMessage(message));
    }
}
