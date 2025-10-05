package com.hrms.jt_construction.service;

import com.hrms.jt_construction.jpa.Department;
import com.hrms.jt_construction.jpa.repos.DepartmentRepository;
import com.hrms.jt_construction.model.request.DepartmentRequet;
import com.hrms.jt_construction.model.response.DepartmentsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class HRMSService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public String createNewDepartment(DepartmentRequet request) {
        Department entity = null;
        entity = departmentRepository.findByDepartmentName(request.getDepartmentName()).orElse(null);
        if (entity == null) {
            entity = new Department();
            entity.setDepartmentName(request.getDepartmentName());
            entity.setSalaryType(request.getSalaryType());
            Department savedDepartment = departmentRepository.save(entity);
            if (savedDepartment.getId() != null)
                return "Department created successfully";
            return "Department creation failed";
        }
        return "Department already exists";
    }

    public List<DepartmentsResponse> getAllDepartmetns() {
        List<DepartmentsResponse> response = new ArrayList<>();
        List<Department> departments = departmentRepository.findAll();
        for (Department department : departments) {
            DepartmentsResponse dptResponse = new DepartmentsResponse();
            dptResponse.setDepartmentId(department.getId());
            dptResponse.setDepartmentName(department.getDepartmentName());
            dptResponse.setSalaryType(department.getSalaryType());
            response.add(dptResponse);
        }
        return response;
    }

    public String editDepartment(DepartmentRequet request) {
        Department entity = null;
        entity = departmentRepository.findByDepartmentName(request.getDepartmentName()).orElse(null);
        if (entity == null)
            return "Department not found";
        int existingID = entity.getId();
        entity = new Department();
        entity.setDepartmentName(request.getDepartmentName());
        entity.setSalaryType(request.getSalaryType());
        entity.setId(existingID);
        Department savedDepartment = departmentRepository.save(entity);
        if (savedDepartment.getId() != null)
            return "Department modified successfully";
        return "Department creation failed";
    }

    @Transactional
    public String deleteDepartment(DepartmentRequet requet) {
        Department entity = null;
        entity = departmentRepository.findByDepartmentName(requet.getDepartmentName()).orElse(null);
        if (entity == null)
            throw new IllegalArgumentException("Department not found");
        departmentRepository.deleteById(entity.getId());
        if (departmentRepository.existsById(entity.getId())) {
            throw new RuntimeException("Deletion failed");
        }
        return "Department deleted successfully";
    }
}
