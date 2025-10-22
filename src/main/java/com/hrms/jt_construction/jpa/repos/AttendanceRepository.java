package com.hrms.jt_construction.jpa.repos;

import com.hrms.jt_construction.jpa.Attendance;
import com.hrms.jt_construction.jpa.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Attendance entity data access.
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    /**
     * Finds all attendance records for a specific employee ID.
     */
    List<Attendance> findByEmployeeId(Long employeeId);

    /**
     * Finds a specific attendance record for an employee on a given date.
     */
    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    /**
     * Finds all attendance records for a given date and status (e.g., 'Absent').
     */
    List<Attendance> findByDateAndStatus(LocalDate date, String status);

    List<Attendance> findByDate(LocalDate date);

    List<Attendance> findByDateAndEmployee_Department(
            LocalDate date,
            String department
    );

    Optional<Attendance> findByEmployeeAndDate(Employee employee, LocalDate date);
}