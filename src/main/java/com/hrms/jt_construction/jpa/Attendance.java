package com.hrms.jt_construction.jpa;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity mapping to the attendance_tbl in the database.
 * NOTE: The employee_tbl reference (Foreign Key) is implied here.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "attendance_tbl")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Maps to BIGINT PRIMARY KEY AUTO_INCREMENT

    // 1. Foreign Key Relationship (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "date", nullable = false)
    private LocalDate date; // Maps to DATE NOT NULL

    @Column(name = "status", length = 10, nullable = false)
    private String status;

    @Column(name = "remarks", length = 250)
    private String remarks; // Maps to VARCHAR(500)

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Maps to TIMESTAMP DEFAULT CURRENT_TIMESTAMP

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Maps to TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
