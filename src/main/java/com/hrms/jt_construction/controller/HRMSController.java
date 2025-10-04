package com.hrms.jt_construction.controller;

import com.hrms.jt_construction.jpa.Department;
import com.hrms.jt_construction.model.request.DepartmentRequet;
import com.hrms.jt_construction.model.response.DepartmentsResponse;
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

    @PostMapping("/create-departments")
    public ResponseEntity<?> addDepartment(@RequestBody DepartmentRequet requet) {
        ResponseMessage responseMessage = null;
        try {
            if (this.hrmsService.createNewDepartment(requet)) {
                responseMessage = new ResponseMessage("Department Created Successfully");
            } else {
                responseMessage = new ResponseMessage("Department Creation Failed");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Internal Server Error: Failed to fetch departments."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(responseMessage);
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
}
