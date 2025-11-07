package com.hrms.jt_construction.jpa;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_tbl")
@Data
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "department")
    private String department;

    @Column(name = "hours_of_work", precision = 5, scale = 2)
    private int hoursOfWork;

    @Column(name = "salary", precision = 10, scale = 2)
    private int salary;

    @Column(name = "salary_type")
    private String salaryType;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        // Sets both timestamps on creation
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Employee() {
    }


    public Employee(String name, String mobile, String department, int hoursOfWork, int salary, String salaryType) {
        this.name = name;
        this.mobile = mobile;
        this.department = department;
        this.hoursOfWork = hoursOfWork;
        this.salary = salary;
        this.salaryType = salaryType;
    }
}