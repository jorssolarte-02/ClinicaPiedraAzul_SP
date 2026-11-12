package com.clinica.backend.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String email;
    @Column(nullable = false) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Role role;
    @Column(nullable = false) private Instant createdAt = Instant.now();

    protected UserAccount() {}
    public UserAccount(String email, String passwordHash, Role role) { this.email=email.toLowerCase().trim(); this.passwordHash=passwordHash; this.role=role; }
    public Long getId(){return id;} public String getEmail(){return email;} public String getPasswordHash(){return passwordHash;} public Role getRole(){return role;}
}
