package com.hrms.jt_construction.service;

import com.hrms.jt_construction.jpa.Department;
import com.hrms.jt_construction.jpa.Employee;
import com.hrms.jt_construction.jpa.repos.DepartmentRepository;
import com.hrms.jt_construction.jpa.repos.EmployeeRepository;
import com.hrms.jt_construction.model.request.DepartmentRequet;
import com.hrms.jt_construction.model.request.EmployeeRequest;
import com.hrms.jt_construction.model.response.DepartmentsResponse;
import com.hrms.jt_construction.model.response.EmployeeResponse;
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

    @Autowired
    private EmployeeRepository employeeRepository;

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
        entity = departmentRepository.findByDepartmentName(request.getModifyDepartmentName()).orElse(null);
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

    public List<EmployeeResponse> getAllEmployeess() {
        List<EmployeeResponse> response = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            EmployeeResponse employeeResponse = new EmployeeResponse();
            employeeResponse.setName(employee.getName());
            employeeResponse.setMobile(employee.getMobile());
            employeeResponse.setDepartment(employee.getDepartment());
            employeeResponse.setHoursOfWork(employee.getHoursOfWork());
            employeeResponse.setSalary(employee.getSalary());
            response.add(employeeResponse);
        }
        return response;
    }

    public String createNewDEmployee(EmployeeRequest request) {
        Employee entity = new Employee();
        entity.setName(request.getName());
        entity.setMobile(request.getMobile());
        entity.setDepartment(request.getDepartment());
        entity.setHoursOfWork(request.getHoursOfWork());
        entity.setSalary(request.getSalary());
        Employee employee = employeeRepository.save(entity);
        if (employee.getId() != null)
            return "Employee added successfully";
        return "Department creation failed";
    }

    public String editDEmployee(EmployeeRequest request) {
        Employee entity = employeeRepository.findById(request.getUniqueID()).orElse(null);
        if (entity == null)
            return "Employee not found";
        long existingID = entity.getId();
        entity = new Employee();
        entity.setName(request.getName());
        entity.setMobile(request.getMobile());
        entity.setDepartment(request.getDepartment());
        entity.setHoursOfWork(request.getHoursOfWork());
        entity.setSalary(request.getSalary());
        entity.setId(existingID);
        Employee employee = employeeRepository.save(entity);
        if (employee.getId() != null)
            return "Employee updated successfully";
        return "Employee updated failed";
    }

    @Transactional
    public String deleteEmployee(EmployeeRequest requet) {
        Employee entity = employeeRepository.findById(requet.getUniqueID()).orElse(null);
        if (entity == null)
            throw new IllegalArgumentException("Employee not found");
        employeeRepository.deleteById(entity.getId());
        if (employeeRepository.existsById(entity.getId()))
            throw new RuntimeException("Deletion failed");
        return "Employee deleted successfully";
    }
}
