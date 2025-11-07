package com.hrms.jt_construction.jpa.repos;

import com.hrms.jt_construction.jpa.Attendance;
import com.hrms.jt_construction.jpa.Employee;
import com.hrms.jt_construction.model.request.MonthlyAttendanceSummary;
import com.hrms.jt_construction.model.response.EmployeeAttendanceProjection;
import com.hrms.jt_construction.model.response.MonthlySalaryCalculationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(
            value = "SELECT " +
                    "A.id AS id, " +
                    "E.id AS employeeId, " +
                    "E.name AS employeeName, " +
                    "E.department AS employeeDepartment, " +
                    "COALESCE(A.status, 'Absent') AS status, " +
                    "A.date AS date, " +
                    "COALESCE(A.remarks, '') AS remarks " +
                    "FROM employee_tbl E " +
                    "LEFT JOIN attendance_tbl A " +
                    "ON E.id = A.employee_id AND A.date = :targetDate " +
                    "WHERE (:department IS NULL OR E.department = :department) " + // <-- NEW OPTIONAL FILTER
                    "ORDER BY E.id",
            nativeQuery = true
    )
    List<EmployeeAttendanceProjection> findEmployeeAttendanceByDate(
            @Param("targetDate") LocalDate targetDate,
            @Param("department") String department // <-- NEW PARAMETER
    );

    Optional<Attendance> findByEmployeeAndDate(Employee employee, LocalDate date);


    @Query(
            value = "SELECT " +
                    "    E.id AS employeeId, " +
                    "    E.name AS employeeName, " +
                    "    E.department AS department, " +
                    "    E.salary AS MonthlySalary, " + // This was monthlySalary, but renamed to reflect it is the base rate
                    "    E.salary_type, " + // <-- This is the added salary_type column in the result set
                    "    A.attendanceMonth, " +
                    "    A.attendanceYear, " +
                    "    A.totalPresentDays, " +
                    "    DAY(LAST_DAY(STR_TO_DATE(CONCAT(:targetYear, '-', :targetMonth, '-01'), '%Y-%m-%d'))) AS totalDaysInMonth, " +
                    "    COALESCE(S.totalAdvance, 0.00) AS totalAdvanceDeduction, " +
                    "    (CASE E.salary_type " +
                    "        WHEN 'Daily' THEN " +
                    "            (E.salary * A.totalPresentDays) " +
                    "        ELSE " + // Handles 'Monthly' and any other unexpected types safely
                    "            ((E.salary / DAY(LAST_DAY(STR_TO_DATE(CONCAT(:targetYear, '-', :targetMonth, '-01'), '%Y-%m-%d')))) * A.totalPresentDays) " +
                    "    END - COALESCE(S.totalAdvance, 0.00)) AS actualSalary " +
                    "FROM employee_tbl E " +
                    "INNER JOIN ( " +
                    "    SELECT " +
                    "        employee_id, " +
                    "        MONTH(date) AS attendanceMonth, " +
                    "        YEAR(date) AS attendanceYear, " +
                    "        COUNT(CASE WHEN status = 'Present' THEN 1 END) AS totalPresentDays " +
                    "    FROM attendance_tbl " +
                    "    WHERE YEAR(date) = :targetYear " +
                    "      AND MONTH(date) = :targetMonth " +
                    "    GROUP BY employee_id, attendanceYear, attendanceMonth " +
                    ") A ON E.id = A.employee_id " +
                    "LEFT JOIN ( " +
                    "    SELECT " +
                    "        employee_id, " +
                    "        SUM(amount) AS totalAdvance " +
                    "    FROM salary_advance_tbl " +
                    "    WHERE YEAR(issued_on) = :targetYear " +
                    "      AND MONTH(issued_on) = :targetMonth " +
                    "    GROUP BY employee_id " +
                    ") S ON E.id = S.employee_id " +
                    "WHERE (:department IS NULL OR E.department = :department)",
            nativeQuery = true
    )
    List<MonthlySalaryCalculationProjection> calculateActualSalaryForMonth(
            @Param("targetYear") Integer targetYear,
            @Param("targetMonth") Integer targetMonth,
            @Param("department") String department // New optional parameter
    );
}