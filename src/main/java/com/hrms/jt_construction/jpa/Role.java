package com.hrms.jt_construction.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "roles_tbl")
@Data
public class Role {
    @Id
    private Integer roleId;
    private String roleName;
}