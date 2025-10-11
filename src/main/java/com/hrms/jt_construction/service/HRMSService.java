package com.hrms.jt_construction.service;

import com.hrms.jt_construction.jpa.Department;
import com.hrms.jt_construction.jpa.Employee;
import com.hrms.jt_construction.jpa.SalaryAdvance;
import com.hrms.jt_construction.jpa.repos.DepartmentRepository;
import com.hrms.jt_construction.jpa.repos.EmployeeRepository;
import com.hrms.jt_construction.jpa.repos.SalaryAdvanceRepository;
import com.hrms.jt_construction.model.request.DepartmentRequet;
import com.hrms.jt_construction.model.request.EmployeeRequest;
import com.hrms.jt_construction.model.request.SalaryAdvanceRequest;
import com.hrms.jt_construction.model.response.DepartmentsResponse;
import com.hrms.jt_construction.model.response.EmployeeResponse;
import com.hrms.jt_construction.model.response.ResponseMessage;
import com.hrms.jt_construction.model.response.SalaryAdvanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class HRMSService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalaryAdvanceRepository salaryAdvanceRepository;

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
        List<Employee> employeeEntityList = employeeRepository.findByDepartment(request.getModifyDepartmentName());
        for (Employee employee : employeeEntityList) {
            employee.setDepartment(request.getDepartmentName());
            employeeRepository.save(employee);
        }
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
            return "Department not found";
        List<Employee> employeeEntityList = employeeRepository.findByDepartment(requet.getDepartmentName());
        for (Employee employee : employeeEntityList) {
            employee.setDepartment(null);
            employeeRepository.save(employee);
        }
        departmentRepository.deleteById(entity.getId());
        if (departmentRepository.existsById(entity.getId())) {
            return "Deletion failed";
        }
        return "Department deleted successfully";
    }

    public List<EmployeeResponse> getAllEmployeess() {
        List<EmployeeResponse> response = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            EmployeeResponse employeeResponse = new EmployeeResponse();
            employeeResponse.setId(employee.getId());
            employeeResponse.setName(employee.getName());
            employeeResponse.setMobile(employee.getMobile());
            employeeResponse.setDepartment(employee.getDepartment());
            employeeResponse.setHoursOfWork(employee.getHoursOfWork());
            employeeResponse.setSalary(employee.getSalary());
            response.add(employeeResponse);
        }
        return response;
    }

    public ResponseMessage createNewDEmployee(EmployeeRequest request) {
        Employee entity = new Employee();
        entity.setName(request.getName());
        entity.setMobile(request.getMobile());
        entity.setDepartment(request.getDepartment());
        entity.setHoursOfWork(request.getHoursOfWork());
        entity.setSalary(request.getSalary());
        Employee employee = employeeRepository.save(entity);
        if (employee.getId() != null) {
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage("Employee created successfully");
            responseMessage.setCreatedID(employee.getId());
            return responseMessage;
        }
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Employee creation failed");
        responseMessage.setCreatedID(-1);
        return responseMessage;
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
        return "Employee update failed";
    }

    @Transactional
    public String deleteEmployee(EmployeeRequest requet) {
        Employee entity = employeeRepository.findById(requet.getUniqueID()).orElse(null);
        if (entity == null)
            return "Employee not found";
        employeeRepository.deleteById(entity.getId());
        if (employeeRepository.existsById(entity.getId()))
            return "Deletion failed";
        return "Employee deleted successfully";
    }

    public List<SalaryAdvanceResponse> getAllSalaryAdvances() {
        List<SalaryAdvanceResponse> responseList = new ArrayList<>();
        List<SalaryAdvance> salaryAdvances = salaryAdvanceRepository.findAll();
        for (SalaryAdvance advance : salaryAdvances) {
            SalaryAdvanceResponse response = new SalaryAdvanceResponse();
            response.setId(advance.getId());
            response.setAmount(advance.getAmount().floatValue());
            response.setRemarks(advance.getRemarks());
            response.setEmployeeName(advance.getEmployee().getName());
            response.setEmployeeDepartment(advance.getEmployee().getDepartment());
            response.setEmployeeId(advance.getEmployee().getId());
            response.setIssuedOn(advance.getIssuedOn());
            responseList.add(response);
        }
        return responseList;
    }

    public String createNewDSalaryAdvance(SalaryAdvanceRequest request) {
        SalaryAdvance entity = new SalaryAdvance();
        entity.setAmount(new BigDecimal(request.getAmount()));
        entity.setRemarks(request.getRemarks());
        entity.setIssuedOn(request.getIssuedOn());
        Employee employee = employeeRepository.findById(request.getEmployeeId()).orElse(null);
        if (employee == null)
            return "Employee not found";
        entity.setEmployee(employee);
        SalaryAdvance createdSalaryAdvance = salaryAdvanceRepository.save(entity);
        if (createdSalaryAdvance.getId() != null)
            return "Salary advance added successfully";
        return "Salary advance creation failed";
    }

    public String editSalaryAdvance(SalaryAdvanceRequest request) {
        SalaryAdvance entity = salaryAdvanceRepository.findById(request.getUniqueID()).orElse(null);
        if (entity == null)
            return "Salary advance not found";
        Employee employee = employeeRepository.findById(request.getEmployeeId()).orElse(null);
        if (employee == null)
            return "Employee not found";
        long existingID = entity.getId();
        entity = new SalaryAdvance();
        entity.setEmployee(employee);
        entity.setAmount(new BigDecimal(request.getAmount()));
        entity.setRemarks(request.getRemarks());
        entity.setIssuedOn(request.getIssuedOn());
        entity.setId(existingID);
        SalaryAdvance modifiedEntity = salaryAdvanceRepository.save(entity);
        if (modifiedEntity.getId() != null)
            return "Salary advance updated successfully";
        return "Salary advance update failed";
    }

    @Transactional
    public String deleteSalaryAdvance(SalaryAdvanceRequest requet) {
        SalaryAdvance entity = salaryAdvanceRepository.findById(requet.getUniqueID()).orElse(null);
        if (entity == null)
            throw new IllegalArgumentException("Salary advance not found");
        salaryAdvanceRepository.deleteById(entity.getId());
        if (salaryAdvanceRepository.existsById(entity.getId()))
            throw new RuntimeException("Deletion failed");
        return "Salary advance deleted successfully";
    }
}
