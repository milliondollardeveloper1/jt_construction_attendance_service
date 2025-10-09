package com.hrms.jt_construction.jpa;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_advance_tbl")
@Data
public class SalaryAdvance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Maps to BIGINT PRIMARY KEY AUTO_INCREMENT

    // 1. Foreign Key Relationship (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "issued_on", nullable = false)
    private LocalDate issuedOn; // Maps to DATE NOT NULL

    @Column(name = "remarks", length = 500)
    private String remarks; // Maps to VARCHAR(500)

    // 3. Timestamp Fields
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public SalaryAdvance() {
    }

    // Constructor for creating new instances (excluding auto-generated fields)
    public SalaryAdvance(Employee employee, BigDecimal amount, LocalDate issuedOn, String remarks) {
        this.employee = employee;
        this.amount = amount;
        this.issuedOn = issuedOn;
        this.remarks = remarks;
    }

}
