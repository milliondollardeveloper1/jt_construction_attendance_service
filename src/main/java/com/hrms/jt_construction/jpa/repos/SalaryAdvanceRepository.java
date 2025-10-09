package com.hrms.jt_construction.jpa.repos;

import com.hrms.jt_construction.jpa.SalaryAdvance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface SalaryAdvanceRepository extends JpaRepository<SalaryAdvance, Long> {

    List<SalaryAdvance> findByEmployeeId(Long employeeId);

    List<SalaryAdvance> findByIssuedOn(LocalDate issuedOn);

    List<SalaryAdvance> findByAmountGreaterThanOrderByIssuedOnDesc(float amount);

    List<SalaryAdvance> findByEmployeeIdAndIssuedOnBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}