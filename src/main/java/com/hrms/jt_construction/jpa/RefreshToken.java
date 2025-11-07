package com.hrms.jt_construction.jpa;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "refresh_token")
@Data
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // The actual token value
    private String token;

    // Links to the User entity (one user can have multiple refresh tokens)
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    // Expiration timestamp (use Instant for timezone-safe storage)
    private Instant expiryDate;
}
