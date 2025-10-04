package com.hrms.jt_construction.service;

import com.hrms.jt_construction.jpa.Department;
import com.hrms.jt_construction.jpa.repos.DepartmentRepository;
import com.hrms.jt_construction.model.request.DepartmentRequet;
import com.hrms.jt_construction.model.response.DepartmentsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HRMSService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public boolean createNewDepartment(DepartmentRequet department) {
        Department entity = new Department();
        entity.setDepartmentName(department.getDepartmentName());
        entity.setSalaryType(department.getSalaryType());
        Department savedDepartment = departmentRepository.save(entity);
        return savedDepartment.getId() != null;
    }

    public List<DepartmentsResponse> getAllDepartmetns() {
        List<DepartmentsResponse> response = new ArrayList<>();
        List<Department> departments = departmentRepository.findAll();
        for (Department department : departments) {
            DepartmentsResponse dptResponse = new DepartmentsResponse();
            dptResponse.setDepartmentId(department.getId());
            department.setDepartmentName(department.getDepartmentName());
            department.setSalaryType(department.getSalaryType());
        }
        return response;
    }
}
