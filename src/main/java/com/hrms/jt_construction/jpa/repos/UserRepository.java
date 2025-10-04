package com.hrms.jt_construction.jpa.repos;

import com.hrms.jt_construction.jpa.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
}