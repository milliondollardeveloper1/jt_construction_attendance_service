package com.hrms.jt_construction.service;

import com.hrms.jt_construction.jpa.Attendance;
import com.hrms.jt_construction.jpa.Department;
import com.hrms.jt_construction.jpa.Employee;
import com.hrms.jt_construction.jpa.SalaryAdvance;
import com.hrms.jt_construction.jpa.repos.AttendanceRepository;
import com.hrms.jt_construction.jpa.repos.DepartmentRepository;
import com.hrms.jt_construction.jpa.repos.EmployeeRepository;
import com.hrms.jt_construction.jpa.repos.SalaryAdvanceRepository;
import com.hrms.jt_construction.model.request.AttendanceRequest;
import com.hrms.jt_construction.model.request.DepartmentRequet;
import com.hrms.jt_construction.model.request.EmployeeRequest;
import com.hrms.jt_construction.model.request.SalaryAdvanceRequest;
import com.hrms.jt_construction.model.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class HRMSService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalaryAdvanceRepository salaryAdvanceRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

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
        return getEmployeeResponses(response, employees);
    }

    public List<EmployeeResponse> getAllEmployeessByDepartment(String departmentName) {
        List<EmployeeResponse> response = new ArrayList<>();
        List<Employee> employees = employeeRepository.findByDepartment(departmentName);
        return getEmployeeResponses(response, employees);
    }

    private List<EmployeeResponse> getEmployeeResponses(List<EmployeeResponse> response, List<Employee> employees) {
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
            response.setIssuedOn(localDateToString(advance.getIssuedOn()));
            responseList.add(response);
        }
        return responseList;
    }

    public ResponseMessage createNewDSalaryAdvance(SalaryAdvanceRequest request) {
        SalaryAdvance entity = new SalaryAdvance();
        ResponseMessage responseMessage = new ResponseMessage();
        entity.setAmount(new BigDecimal(request.getAmount()));
        entity.setRemarks(request.getRemarks());
        entity.setIssuedOn(stringToLocalDate(request.getIssuedOn()));
        Employee employee = employeeRepository.findById(request.getEmployeeId()).orElse(null);
        if (employee == null) {
            responseMessage.setMessage("Employee not found");
            responseMessage.setCreatedID(-1);
            return responseMessage;
        }
        entity.setEmployee(employee);
        SalaryAdvance createdSalaryAdvance = salaryAdvanceRepository.save(entity);
        if (createdSalaryAdvance.getId() != null) {
            responseMessage.setMessage("Salary advance added successfully");
            responseMessage.setCreatedID(employee.getId());
            return responseMessage;
        }
        responseMessage.setMessage("Salary advance creation failed");
        responseMessage.setCreatedID(-1);
        return responseMessage;
    }

    public static LocalDate stringToLocalDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH));
    }

    public static String localDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH));
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
        entity.setIssuedOn(stringToLocalDate(request.getIssuedOn()));
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

    public List<AttendanceResponse> getAllAttendance(String date, String department) {
        List<AttendanceResponse> responseList = new ArrayList<>();
        if (date == null || date.isEmpty())
            throw new IllegalArgumentException("Date is empty");
        if (department == null || department.equalsIgnoreCase("ALL") || department.isEmpty()) {
            List<Attendance> attendanceList = this.attendanceRepository.findByDate(stringToLocalDate(date));
            return getAttendanceResponses(responseList, attendanceList);
        }else {
            List<Attendance> attendanceList = this.attendanceRepository.findByDateAndEmployee_Department(stringToLocalDate(date), department);
            return getAttendanceResponses(responseList, attendanceList);
        }

    }

    private List<AttendanceResponse> getAttendanceResponses(List<AttendanceResponse> responseList, List<Attendance> attendanceList) {
        for (Attendance attendance : attendanceList) {
            AttendanceResponse attendanceResponse = new AttendanceResponse();
            attendanceResponse.setId(attendance.getId());
            attendanceResponse.setEmployeeId(attendance.getEmployee().getId());
            attendanceResponse.setEmployeeName(attendance.getEmployee().getName());
            attendanceResponse.setDate(localDateToString(attendance.getDate()));
            attendanceResponse.setStatus(attendance.getStatus());
            attendanceResponse.setRemarks(attendance.getRemarks());
            attendanceResponse.setEmployeeDepartment(attendance.getEmployee().getDepartment());
            responseList.add(attendanceResponse);
        }
        return responseList;
    }

    @Transactional
    public String updateAttendance(List<AttendanceRequest> requestList) {
        ResponseMessage responseMessage = new ResponseMessage();
        List<Attendance> recordsToSave = new ArrayList<>();

        for (AttendanceRequest request : requestList) {

            Employee employee = this.employeeRepository.findById(request.getEmployeeId()).orElse(null);
            if (employee == null) {
                return "Employee ID " + request.getEmployeeId() + " not found.";
            }

            LocalDate attendanceDate = stringToLocalDate(request.getDate());

            Optional<Attendance> existingRecord =
                    this.attendanceRepository.findByEmployeeAndDate(employee, attendanceDate);

            Attendance attendance;

            if (existingRecord.isPresent()) {
                attendance = existingRecord.get();
            } else {
                attendance = new Attendance();
                attendance.setEmployee(employee);
                attendance.setDate(attendanceDate);
            }

            attendance.setStatus(request.getStatus());
            attendance.setRemarks(request.getRemarks());

            recordsToSave.add(attendance);
        }

        this.attendanceRepository.saveAll(recordsToSave);

        return "Attendance batch processed successfully.";
    }
}
