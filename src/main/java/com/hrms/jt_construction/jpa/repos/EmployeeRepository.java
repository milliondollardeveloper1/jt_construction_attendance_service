package com.hrms.jt_construction.jpa.repos;

import com.hrms.jt_construction.jpa.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByName(String name);

    List<Employee> findByDepartmentOrderBySalaryDesc(String department);
}
