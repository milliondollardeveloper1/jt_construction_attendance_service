package com.hrms.jt_construction.jpa.repos;

import com.hrms.jt_construction.jpa.RefreshToken;
import com.hrms.jt_construction.jpa.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser_Id(Long id);
}
