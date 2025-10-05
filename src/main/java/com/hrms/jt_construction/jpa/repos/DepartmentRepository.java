package com.hrms.jt_construction.jpa.repos;

import com.hrms.jt_construction.jpa.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findById(Integer id);

    Optional<Department> findByDepartmentName(String departmentName);

    Optional<Department> findByDepartmentNameContainingIgnoreCase(String departmentName);

    List<Department> findByDepartmentNameContainingIgnoreCaseAndSalaryTypeContainingIgnoreCase(
            String nameKeyword,
            String salaryKeyword
    );

    void deleteById(int id);
}
